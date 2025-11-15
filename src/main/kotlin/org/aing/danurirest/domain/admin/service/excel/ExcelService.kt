package org.aing.danurirest.domain.admin.service.excel

import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.admin.service.excel.config.ExcelProperties
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.entity.Form
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly = true)
class ExcelService(
    private val properties: ExcelProperties,
    private val templateLoader: ExcelTemplateLoader,
    private val styleProvider: ExcelStyleProvider,
    private val formDataParser: FormDataParser,
    private val cellWriter: ExcelCellWriter,
    private val statisticsAggregator: UsageStatisticsAggregator,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ExcelService::class.java)
    }

    /**
     * 이용 내역 Excel 생성
     * @param usageHistories 이용 내역 목록
     * @param signUpForm 가입 폼 정보
     * @return Excel 파일 바이트 배열
     * @throws ExcelGenerationException Excel 생성 실패 시
     */
    fun createUsageHistoryExcel(
        usageHistories: List<UsageHistoryResponse>,
        signUpForm: Form,
    ): ByteArray {
        logger.info("이용 내역 Excel 생성 시작: formId=${signUpForm.id}, 데이터 수=${usageHistories.size}")

        try {
            val workbook = templateLoader.createEmptyWorkbook()
            val sheet = workbook.createSheet("이용 내역")

            // 스타일 생성
            val headerStyle = styleProvider.createHeaderStyle(workbook)

            // 폼 라벨 추출
            val formLabels = formDataParser.extractFormLabels(signUpForm)

            // 헤더 작성
            writeUsageHistoryHeader(sheet, formLabels, headerStyle)

            // 데이터 작성
            val dateFormatter = DateTimeFormatter.ofPattern(properties.format.dateTime)
            writeUsageHistoryData(sheet, usageHistories, formLabels, dateFormatter)

            // 컬럼 너비 자동 조정
            autoSizeColumns(sheet, formLabels.size + 5)

            logger.info("이용 내역 Excel 생성 완료")

            return convertWorkbookToByteArray(workbook)
        } catch (e: Exception) {
            logger.error("이용 내역 Excel 생성 실패", e)
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    fun createMonthlyUsageExcel(
        space: Space,
        yearMonth: YearMonth,
        usageHistories: List<UsageHistory>,
    ): ByteArray {
        logger.info(
            "월별 이용 현황 생성 시작: space=${space.name}, yearMonth=$yearMonth, 이용 내역 수=${usageHistories.size}",
        )

        try {
            // 템플릿 로드
            val workbook = templateLoader.loadMonthlyUsageTemplate()
            val sheet = workbook.getSheetAt(0) ?: throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)

            // 스타일 생성
            val redFontStyle = styleProvider.createRedFontStyle(workbook)
            val blueFontStyle = styleProvider.createBlueFontStyle(workbook)

            // 타이틀 설정
            writeMonthlyUsageTitle(sheet, space, yearMonth)

            // 통계 집계
            val aggregationResult = statisticsAggregator.aggregateDailyStatistics(usageHistories, yearMonth)

            // 합계 행 백업
            val summaryRows = backupSummaryRows(sheet)

            // 데이터 작성
            writeDailyStatisticsData(
                sheet,
                yearMonth,
                aggregationResult.dailyStats,
                redFontStyle,
                blueFontStyle,
            )

            // 불필요한 날짜 행 처리
            handleExtraDays(sheet, yearMonth.lengthOfMonth())

            // 합계 행 재배치
            val newSummaryStartRow = properties.data.startRow + yearMonth.lengthOfMonth() + properties.data.summaryRowOffset
            cellWriter.updateSummaryRows(sheet, summaryRows, newSummaryStartRow, yearMonth.lengthOfMonth())

            logger.info("월별 이용 현황 생성 완료")

            return convertWorkbookToByteArray(workbook)
        } catch (e: Exception) {
            logger.error("월별 이용 현황 생성 실패", e)
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    private fun writeUsageHistoryHeader(
        sheet: Sheet,
        formLabels: List<String>,
        headerStyle: CellStyle,
    ) {
        val headerRow = sheet.createRow(0)
        val baseHeaders = listOf("공간 이름", "시작 시간", "종료 시간", "대여 항목 수", "연락처")
        val allHeaders = baseHeaders + formLabels

        allHeaders.forEachIndexed { idx, header ->
            cellWriter.setCellValue(headerRow, idx, header, headerStyle)
        }
    }

    private fun writeUsageHistoryData(
        sheet: Sheet,
        usageHistories: List<UsageHistoryResponse>,
        formLabels: List<String>,
        dateFormatter: DateTimeFormatter,
    ) {
        val baseHeadersSize = 5

        usageHistories.forEachIndexed { index, history ->
            val row = sheet.createRow(index + 1)

            // 기본 정보 작성
            cellWriter.setCellValue(row, 0, history.spaceName)
            cellWriter.setCellValue(row, 1, history.startAt.format(dateFormatter))
            cellWriter.setCellValue(
                row,
                2,
                history.endAt?.format(dateFormatter) ?: "사용 중",
            )
            cellWriter.setCellValue(row, 3, history.rentalCount.toDouble())
            cellWriter.setCellValue(row, 4, history.userPhone)

            // 폼 결과 작성
            writeFormResultData(row, history.formResult, formLabels, baseHeadersSize)
        }
    }

    private fun writeFormResultData(
        row: Row,
        formResultJson: String,
        formLabels: List<String>,
        baseHeadersSize: Int,
    ) {
        try {
            val formResult = formDataParser.parseFormResult(formResultJson)

            formLabels.forEachIndexed { formIdx, label ->
                val answer = formResult.get(label)?.asText() ?: ""
                cellWriter.setCellValue(row, baseHeadersSize + formIdx, answer)
            }
        } catch (e: Exception) {
            logger.error("폼 결과 파싱 실패: row=${row.rowNum}", e)
            formLabels.forEachIndexed { formIdx, _ ->
                cellWriter.setCellValue(row, baseHeadersSize + formIdx, "")
            }
        }
    }

    private fun writeMonthlyUsageTitle(
        sheet: Sheet,
        space: Space,
        yearMonth: YearMonth,
    ) {
        val title = "${yearMonth.year}년 ${yearMonth.monthValue}월 이용자현황 - ${space.name}"
        val titleRow = sheet.getRow(0) ?: sheet.createRow(0)
        val titleCell = titleRow.getCell(0) ?: titleRow.createCell(0)
        titleCell.setCellValue(title)
    }

    private fun writeDailyStatisticsData(
        sheet: Sheet,
        yearMonth: YearMonth,
        dailyStats: Map<Int, Map<Age, Map<Sex, Int>>>,
        redFontStyle: CellStyle,
        blueFontStyle: CellStyle,
    ) {
        val daysInMonth = yearMonth.lengthOfMonth()
        val year = yearMonth.year
        val month = yearMonth.monthValue

        for (day in 1..daysInMonth) {
            val rowIndex = properties.data.startRow + day - 1
            val row = sheet.getRow(rowIndex) ?: sheet.createRow(rowIndex)

            val stats = dailyStats[day] ?: emptyMap()

            cellWriter.writeDailyStatistics(
                row,
                day,
                year,
                month,
                stats,
                redFontStyle,
                blueFontStyle,
            )
        }
    }

    private fun backupSummaryRows(sheet: Sheet): List<Row> {
        val summaryRowIndex = properties.data.startRow + 31 + properties.data.summaryRowOffset
        val summaryRows = mutableListOf<Row>()

        for (i in summaryRowIndex..sheet.lastRowNum) {
            sheet.getRow(i)?.let { row ->
                if (!cellWriter.isEmptyRow(row)) {
                    summaryRows.add(row)
                }
            }
        }

        return summaryRows
    }

    private fun handleExtraDays(
        sheet: Sheet,
        daysInMonth: Int,
    ) {
        if (daysInMonth >= 31) {
            return
        }

        for (day in (daysInMonth + 1)..31) {
            val rowIndex = properties.data.startRow + day - 1
            val row = sheet.getRow(rowIndex)
            row?.let { clearRow(it) }
        }

        if (sheet.lastRowNum > properties.data.startRow + daysInMonth) {
            sheet.shiftRows(
                properties.data.startRow + 31,
                sheet.lastRowNum,
                -(31 - daysInMonth),
            )
        }
    }

    private fun clearRow(row: Row) {
        for (cellNum in 0 until row.lastCellNum) {
            val cell = row.getCell(cellNum)
            cell?.let { row.removeCell(it) }
        }
    }

    private fun autoSizeColumns(
        sheet: Sheet,
        columnCount: Int,
    ) {
        for (i in 0 until columnCount) {
            sheet.autoSizeColumn(i)
        }
    }

    private fun convertWorkbookToByteArray(workbook: XSSFWorkbook): ByteArray =
        workbook.use { wb ->
            ByteArrayOutputStream().use { outputStream ->
                wb.write(outputStream)
                outputStream.toByteArray()
            }
        }
}
