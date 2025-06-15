package org.aing.danurirest.domain.admin.service

import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

@Service
class ExcelService {
    fun createUsageHistoryExcel(usageHistories: List<UsageHistoryResponse>): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("이용 히스토리")

        // 헤더 스타일 설정
        val headerStyle = workbook.createCellStyle()
        headerStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        // 헤더 생성
        val headerRow = sheet.createRow(0)
        val headers =
            listOf(
                "사용자 이름",
                "사용자 연락처",
                "공간 이름",
                "시작 시간",
                "종료 시간",
                "대여 항목 수",
            )

        headers.forEachIndexed { idx, header ->
            val cell = headerRow.createCell(idx)
            cell.setCellValue(header)
            cell.cellStyle = headerStyle
        }

        // 데이터 채우기
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        usageHistories.forEachIndexed { index, history ->
            val row = sheet.createRow(index + 1)

            row.createCell(0).setCellValue(history.userName)
            row.createCell(1).setCellValue(history.userPhone)
            row.createCell(2).setCellValue(history.spaceName)
            row.createCell(3).setCellValue(history.startAt.format(dateFormatter))
            row.createCell(4).setCellValue(history.endAt?.format(dateFormatter) ?: "사용 중")
            row.createCell(5).setCellValue(history.rentalCount.toDouble())
        }

        // 열 너비 자동 조정
        for (i in headers.indices) {
            sheet.autoSizeColumn(i)
        }

        // 바이트 배열로 변환
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()

        return outputStream.toByteArray()
    }
}
