package org.aing.danurirest.domain.admin.service.excel

import org.aing.danurirest.domain.admin.service.excel.config.ExcelProperties
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import org.apache.poi.ss.usermodel.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Excel 셀 작성을 담당하는 컴포넌트
 */
@Component
class ExcelCellWriter(
    private val properties: ExcelProperties,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ExcelCellWriter::class.java)
    }

    /**
     * 문자열 값으로 셀 설정
     */
    fun setCellValue(
        row: Row,
        columnIndex: Int,
        value: String,
        style: CellStyle? = null,
    ) {
        try {
            val cell = row.getCell(columnIndex) ?: row.createCell(columnIndex)
            cell.setCellValue(value)
            style?.let { cell.cellStyle = it }
        } catch (e: Exception) {
            logger.error("셀 작성 실패: row=${row.rowNum}, col=$columnIndex, value=$value", e)
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    /**
     * 숫자 값으로 셀 설정
     */
    fun setCellValue(
        row: Row,
        columnIndex: Int,
        value: Double,
        style: CellStyle? = null,
    ) {
        try {
            val cell = row.getCell(columnIndex) ?: row.createCell(columnIndex)
            cell.setCellValue(value)
            style?.let { cell.cellStyle = it }
        } catch (e: Exception) {
            logger.error("셀 작성 실패: row=${row.rowNum}, col=$columnIndex, value=$value", e)
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    /**
     * 일별 통계 데이터를 행에 작성
     * @return 해당 일의 총 인원 수
     */
    fun writeDailyStatistics(
        row: Row,
        day: Int,
        year: Int,
        month: Int,
        dailyStats: Map<Age, Map<Sex, Int>>,
        redFontStyle: CellStyle,
        blueFontStyle: CellStyle,
    ): Int {
        val date = LocalDate.of(year, month, day)
        val dayOfWeekKorean = getDayOfWeekKorean(date.dayOfWeek)
        val isMonday = date.dayOfWeek == DayOfWeek.MONDAY

        // 날짜 설정
        setCellValue(row, 0, day.toString())

        // 요일 설정 (토요일 파란색, 일요일 빨간색)
        val dayOfWeekStyle =
            when (dayOfWeekKorean) {
                "토" -> blueFontStyle
                "일" -> redFontStyle
                else -> null
            }
        setCellValue(row, 1, dayOfWeekKorean, dayOfWeekStyle)

        // 운영 상태 설정 (휴관일 때만 빨간색)
        val operationStatus = if (isMonday) "휴관" else "운영"
        setCellValue(row, 2, operationStatus, if (isMonday) redFontStyle else null)

        // 연령별/성별 통계 입력
        var colIndex = 3
        var dayTotal = 0

        Age.entries.forEach { age ->
            val sexMap = dailyStats[age] ?: emptyMap()
            val male = sexMap[Sex.MALE] ?: 0
            val female = sexMap[Sex.FEMALE] ?: 0

            setCellValue(row, colIndex, male.toDouble())
            setCellValue(row, colIndex + 1, female.toDouble())

            dayTotal += male + female
            colIndex += 2

            if (age == Age.OUT_OF_SCHOOL_YOUTH) {
                colIndex += 2
            }
        }

        if (dayTotal > 0) {
            logger.debug("Day $day 총 인원: $dayTotal")
        }

        return dayTotal
    }

    /**
     * 합계 행 업데이트
     */
    fun updateSummaryRows(
        sheet: Sheet,
        summaryRows: List<Row>,
        newStartRow: Int,
        daysInMonth: Int,
    ) {
        val dataStartRow = properties.data.startRow

        summaryRows.forEachIndexed { index, oldRow ->
            val newRowIndex = newStartRow + index
            val newRow = sheet.getRow(newRowIndex) ?: sheet.createRow(newRowIndex)

            for (cell in oldRow) {
                if (cell != null) {
                    copyCellWithFormulaUpdate(cell, newRow, dataStartRow, daysInMonth)
                }
            }
        }
    }

    /**
     * 셀 복사 및 수식 업데이트
     */
    private fun copyCellWithFormulaUpdate(
        sourceCell: Cell,
        targetRow: Row,
        dataStartRow: Int,
        daysInMonth: Int,
    ) {
        val targetCell =
            targetRow.getCell(sourceCell.columnIndex)
                ?: targetRow.createCell(sourceCell.columnIndex)

        when (sourceCell.cellType) {
            CellType.FORMULA -> {
                val updatedFormula =
                    updateFormula(
                        sourceCell.cellFormula,
                        dataStartRow + 1,
                        dataStartRow + daysInMonth,
                    )
                try {
                    targetCell.cellFormula = updatedFormula
                } catch (e: Exception) {
                    logger.error("수식 설정 실패: $updatedFormula", e)
                    throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
                }
            }
            CellType.NUMERIC -> targetCell.setCellValue(sourceCell.numericCellValue)
            CellType.STRING -> targetCell.setCellValue(sourceCell.stringCellValue)
            CellType.BOOLEAN -> targetCell.setCellValue(sourceCell.booleanCellValue)
            else -> {}
        }

        // 스타일 복사
        targetCell.cellStyle = sourceCell.cellStyle
    }

    /**
     * SUM 수식의 범위 업데이트
     */
    fun updateFormula(
        formula: String,
        startRow: Int,
        endRow: Int,
    ): String =
        formula.replace(Regex("SUM\\(([A-Z]+)\\d+:([A-Z]+)\\d+\\)")) { match ->
            val column1 = match.groupValues[1]
            val column2 = match.groupValues[2]
            "SUM(${column1}$startRow:${column2}$endRow)"
        }

    /**
     * 요일을 한글로 변환
     */
    private fun getDayOfWeekKorean(dayOfWeek: DayOfWeek): String =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> "월"
            DayOfWeek.TUESDAY -> "화"
            DayOfWeek.WEDNESDAY -> "수"
            DayOfWeek.THURSDAY -> "목"
            DayOfWeek.FRIDAY -> "금"
            DayOfWeek.SATURDAY -> "토"
            DayOfWeek.SUNDAY -> "일"
        }

    /**
     * 행이 비어있는지 확인
     */
    fun isEmptyRow(row: Row): Boolean {
        for (cell in row) {
            if (cell != null && cell.cellType != CellType.BLANK) {
                val stringValue = cell.toString().trim()
                if (stringValue.isNotEmpty()) {
                    return false
                }
            }
        }
        return true
    }
}
