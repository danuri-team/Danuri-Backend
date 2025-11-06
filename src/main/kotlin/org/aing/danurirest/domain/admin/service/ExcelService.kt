package org.aing.danurirest.domain.admin.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.persistence.form.entity.Form
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Service
class ExcelService(
    private val objectMapper: ObjectMapper,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ExcelService::class.java)
        private const val DATA_START_ROW = 4
        private const val SUMMARY_ROW_OFFSET = 1
    }

    fun createUsageHistoryExcel(
        usageHistories: List<UsageHistoryResponse>,
        signUpForm: Form,
    ): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("이용 내역")

        val headerStyle =
            workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
            }

        val formLabels = mutableListOf<String>()

        try {
            val formSchema = objectMapper.readTree(signUpForm.formSchema)
            if (formSchema?.isArray == true) {
                formSchema.forEach { field ->
                    field?.get("label")?.asText()?.let { label ->
                        formLabels.add(label)
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("폼 스키마 파싱 실패", e)
        }

        val headerRow = sheet.createRow(0)
        val baseHeaders = listOf("공간 이름", "시작 시간", "종료 시간", "대여 항목 수", "연락처")
        val allHeaders = baseHeaders + formLabels

        allHeaders.forEachIndexed { idx, header ->
            headerRow.createCell(idx).apply {
                setCellValue(header)
                cellStyle = headerStyle
            }
        }

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        usageHistories.forEachIndexed { index, history ->
            val row = sheet.createRow(index + 1)

            row.createCell(0).setCellValue(history.spaceName)
            row.createCell(1).setCellValue(
                history.startAt.format(dateFormatter) ?: "",
            )
            row.createCell(2).setCellValue(
                history.endAt?.format(dateFormatter) ?: "사용 중",
            )
            row.createCell(3).setCellValue(history.rentalCount.toDouble())
            row.createCell(4).setCellValue(history.userPhone)

            try {
                val formResult = history.formResult.let { objectMapper.readTree(it) }
                formLabels.forEachIndexed { formIdx, label ->
                    val answer = formResult?.get(label)?.asText() ?: ""
                    row.createCell(baseHeaders.size + formIdx).setCellValue(answer)
                }
            } catch (e: Exception) {
                logger.error("폼 결과 파싱 실패", e)
                formLabels.forEachIndexed { formIdx, _ ->
                    row.createCell(baseHeaders.size + formIdx).setCellValue("")
                }
            }
        }

        for (i in allHeaders.indices) {
            sheet.autoSizeColumn(i)
        }

        return workbook.use { wb ->
            ByteArrayOutputStream().use { outputStream ->
                wb.write(outputStream)
                outputStream.toByteArray()
            }
        }
    }

    fun createMonthlyUsageExcel(
        space: Space,
        yearMonth: YearMonth,
        usageHistories: List<UsageHistory>,
    ): ByteArray {
        logger.info("월별 이용 현황 생성 시작: ${space.name}, $yearMonth, 이용 내역 수: ${usageHistories.size}")

        val templateResource = ClassPathResource("template/space_usage_history.xlsx")
        val workbook = XSSFWorkbook(templateResource.inputStream)
        val sheet = workbook.getSheetAt(0) ?: throw IllegalStateException("템플릿 시트를 찾을 수 없습니다")

        val redFontStyle =
            workbook.createCellStyle().apply {
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN

                setFont(
                    workbook.createFont().apply {
                        setColor(XSSFColor(byteArrayOf(255.toByte(), 0, 0), null))
                        fontName = "맑은 고딕"
                        fontHeightInPoints = 10
                    },
                )
            }

        val blueFontStyle =
            workbook.createCellStyle().apply {
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN

                setFont(
                    workbook.createFont().apply {
                        setColor(XSSFColor(byteArrayOf(0, 0, 255.toByte()), null))
                        fontName = "맑은 고딕"
                        fontHeightInPoints = 10
                    },
                )
            }

        val year = yearMonth.year
        val month = yearMonth.monthValue
        val title = "${year}년 ${month}월 이용자현황 - ${space.name}"

        val titleRow = sheet.getRow(0) ?: sheet.createRow(0)
        val titleCell = titleRow.getCell(0) ?: titleRow.createCell(0)
        titleCell.setCellValue(title)

        val daysInMonth = yearMonth.lengthOfMonth()

        val dailyStats = mutableMapOf<Int, MutableMap<Age, MutableMap<Sex, Int>>>()
        for (day in 1..daysInMonth) {
            dailyStats[day] = mutableMapOf()
            Age.entries.forEach { age ->
                dailyStats[day]!![age] =
                    mutableMapOf(
                        Sex.MALE to 0,
                        Sex.FEMALE to 0,
                    )
            }
        }

        var processedCount = 0
        var skippedCount = 0

        usageHistories.forEach { usage ->
            try {
                val startAt = usage.startAt

                val day = startAt.dayOfMonth

                if (day < 1 || day > daysInMonth) {
                    logger.warn("유효하지 않은 날짜: $day (월의 날짜 수: $daysInMonth)")
                    skippedCount++
                    return@forEach
                }

                // 메인 사용자 처리
                val formResult = usage.user.signUpForm?.result
                val userAge = getUserAgeFromFormResult(formResult)
                val userSex = getUserSexFromFormResult(formResult)

                if (userAge != null && userSex != null) {
                    val ageStats = dailyStats[day]?.get(userAge)
                    if (ageStats != null) {
                        val currentCount = ageStats[userSex] ?: 0
                        ageStats[userSex] = currentCount + 1
                        logger.debug(
                            "Day {}: 사용자 추가 - {}/{} (현재 카운트: {})",
                            day,
                            userAge,
                            userSex,
                            currentCount + 1,
                        )
                        processedCount++
                    }
                } else {
                    logger.debug(
                        "Day {}: 사용자 연령/성별 정보 없음 (Age: {}, Sex: {})",
                        day,
                        userAge,
                        userSex,
                    )
                }

                // 추가 참가자 처리
                usage.additionalParticipants.forEach { participant ->
                    try {
                        val participantAge = participant.ageGroup
                        val participantSex = participant.sex
                        val count = participant.count

                        if (count > 0) {
                            val ageStats = dailyStats[day]?.get(participantAge)
                            if (ageStats != null) {
                                val currentCount = ageStats[participantSex] ?: 0
                                ageStats[participantSex] = currentCount + count
                                logger.debug(
                                    "Day {}: 추가 참가자 - {}/{} x {}",
                                    day,
                                    participantAge,
                                    participantSex,
                                    count,
                                )
                            }
                        }
                    } catch (e: Exception) {
                        logger.error("추가 참가자 처리 중 오류", e)
                    }
                }
            } catch (e: Exception) {
                logger.error("이용 내역 처리 중 오류", e)
                skippedCount++
            }
        }

        logger.info("통계 집계 완료 - 처리: $processedCount, 건너뜀: $skippedCount")

        // 기존 합계 행들 백업
        val summaryRowIndex = DATA_START_ROW + 31 + SUMMARY_ROW_OFFSET
        val summaryRows = mutableListOf<Row>()

        for (i in summaryRowIndex..sheet.lastRowNum) {
            sheet.getRow(i)?.let { row ->
                if (!isEmptyRow(row)) {
                    summaryRows.add(row)
                }
            }
        }

        // 데이터 입력
        for (day in 1..daysInMonth) {
            val rowIndex = DATA_START_ROW + day - 1
            val row = sheet.getRow(rowIndex) ?: sheet.createRow(rowIndex)

            val date = LocalDate.of(year, month, day)
            val dayOfWeekKorean =
                when (date.dayOfWeek) {
                    DayOfWeek.MONDAY -> "월"
                    DayOfWeek.TUESDAY -> "화"
                    DayOfWeek.WEDNESDAY -> "수"
                    DayOfWeek.THURSDAY -> "목"
                    DayOfWeek.FRIDAY -> "금"
                    DayOfWeek.SATURDAY -> "토"
                    DayOfWeek.SUNDAY -> "일"
                    else -> ""
                }
            val isMonday = date.dayOfWeek == DayOfWeek.MONDAY

            // 날짜 설정 (기본 색상)
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

            // 연령별/성별 통계 입력 (기본 색상)
            val stats = dailyStats[day]
            if (stats != null) {
                var colIndex = 3
                var dayTotal = 0

                Age.entries.forEach { age ->
                    val sexMap = stats[age] ?: mutableMapOf()
                    val male = sexMap[Sex.MALE] ?: 0
                    val female = sexMap[Sex.FEMALE] ?: 0

                    setCellValue(row, colIndex, male.toDouble())
                    setCellValue(row, colIndex + 1, female.toDouble())

                    dayTotal += male + female
                    colIndex += 2
                }

                if (dayTotal > 0) {
                    logger.debug("Day $day 총 인원: $dayTotal")
                }
            } else {
                var colIndex = 3
                Age.entries.forEach { _ ->
                    setCellValue(row, colIndex, 0.0)
                    setCellValue(row, colIndex + 1, 0.0)
                    colIndex += 2
                }
            }
        }

        // 필요없는 날짜 행 처리 - 빈 행으로 만들기 (제거 대신)
        if (daysInMonth < 31) {
            for (day in (daysInMonth + 1)..31) {
                val rowIndex = DATA_START_ROW + day - 1
                val row = sheet.getRow(rowIndex)
                if (row != null) {
                    // 행을 완전히 제거하는 대신 모든 셀의 내용을 비움
                    for (cellNum in 0 until row.lastCellNum) {
                        val cell = row.getCell(cellNum)
                        if (cell != null) {
                            row.removeCell(cell)
                        }
                    }
                }
            }

            // 불필요한 행들을 위로 shift하여 빈 공간 제거
            if (sheet.lastRowNum > DATA_START_ROW + daysInMonth) {
                sheet.shiftRows(
                    DATA_START_ROW + 31,
                    sheet.lastRowNum,
                    -(31 - daysInMonth),
                )
            }
        }

        // 합계 행 재배치
        val newSummaryStartRow = DATA_START_ROW + daysInMonth + SUMMARY_ROW_OFFSET
        updateSummaryRows(sheet, summaryRows, newSummaryStartRow, daysInMonth)

        logger.info("월별 이용 현황 생성 완료")

        return workbook.use { wb ->
            ByteArrayOutputStream().use { out ->
                wb.write(out)
                out.toByteArray()
            }
        }
    }

    private fun setCellValue(
        row: Row,
        columnIndex: Int,
        value: String,
        style: CellStyle? = null,
    ) {
        val cell = row.getCell(columnIndex) ?: row.createCell(columnIndex)
        cell.setCellValue(value)
        style?.let { cell.cellStyle = it }
    }

    private fun setCellValue(
        row: Row,
        columnIndex: Int,
        value: Double,
        style: CellStyle? = null,
    ) {
        val cell = row.getCell(columnIndex) ?: row.createCell(columnIndex)
        cell.setCellValue(value)
        style?.let { cell.cellStyle = it }
    }

    private fun isEmptyRow(row: Row): Boolean {
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

    private fun updateSummaryRows(
        sheet: Sheet,
        summaryRows: List<Row>,
        newStartRow: Int,
        daysInMonth: Int,
    ) {
        summaryRows.forEachIndexed { index, oldRow ->
            val newRowIndex = newStartRow + index
            val newRow = sheet.getRow(newRowIndex) ?: sheet.createRow(newRowIndex)

            for (cell in oldRow) {
                if (cell != null) {
                    val newCell =
                        newRow.getCell(cell.columnIndex)
                            ?: newRow.createCell(cell.columnIndex)

                    when (cell.cellType) {
                        CellType.FORMULA -> {
                            val formula =
                                updateFormula(
                                    cell.cellFormula,
                                    DATA_START_ROW + 1,
                                    DATA_START_ROW + daysInMonth,
                                )
                            try {
                                newCell.cellFormula = formula
                            } catch (e: Exception) {
                                logger.error("수식 설정 실패: $formula", e)
                                newCell.setCellValue(0.0)
                            }
                        }
                        CellType.NUMERIC -> newCell.setCellValue(cell.numericCellValue)
                        CellType.STRING -> newCell.setCellValue(cell.stringCellValue)
                        CellType.BOOLEAN -> newCell.setCellValue(cell.booleanCellValue)
                        else -> {}
                    }
                }
            }
        }
    }

    private fun updateFormula(
        formula: String,
        startRow: Int,
        endRow: Int,
    ): String =
        formula.replace(Regex("SUM\\(([A-Z]+)\\d+:([A-Z]+)\\d+\\)")) { match ->
            val column1 = match.groupValues[1]
            val column2 = match.groupValues[2]
            "SUM(${column1}$startRow:${column2}$endRow)"
        }

    private fun getUserAgeFromFormResult(formResultJson: String?): Age? {
        if (formResultJson.isNullOrBlank()) return null

        return try {
            val formResult = objectMapper.readTree(formResultJson)
            val schoolOption = formResult?.get("나이")?.asText()

            val age =
                when (schoolOption) {
                    "초등학교" -> Age.ELEMENTARY
                    "중학교" -> Age.MIDDLE
                    "고등학교" -> Age.HIGH
                    "대학교" -> Age.COLLEGE
                    "학교 밖 청소년" -> Age.OUT_OF_SCHOOL_YOUTH
                    "성인/유아" -> Age.ADULT
                    else -> null
                }

            if (age != null) {
                logger.trace("연령 파싱 성공: {} -> {}", schoolOption, age)
            }
            age
        } catch (e: Exception) {
            logger.error("연령 파싱 실패: $formResultJson", e)
            null
        }
    }

    private fun getUserSexFromFormResult(formResultJson: String?): Sex? {
        if (formResultJson.isNullOrBlank()) return null

        return try {
            val formResult = objectMapper.readTree(formResultJson)
            val sexOption = formResult?.get("성별")?.asText()

            val sex =
                when (sexOption) {
                    "남" -> Sex.MALE
                    "여" -> Sex.FEMALE
                    else -> null
                }

            if (sex != null) {
                logger.trace("성별 파싱 성공: {} -> {}", sexOption, sex)
            }
            sex
        } catch (e: Exception) {
            logger.error("성별 파싱 실패: $formResultJson", e)
            null
        }
    }
}
