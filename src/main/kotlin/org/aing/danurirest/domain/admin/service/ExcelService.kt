package org.aing.danurirest.domain.admin.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.persistence.form.entity.Form
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

@Service
class ExcelService {
    private val objectMapper = ObjectMapper()

    fun createUsageHistoryExcel(
        usageHistories: List<UsageHistoryResponse>,
        signUpForm: Form,
    ): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("이용 내역")

        val headerStyle = workbook.createCellStyle()
        headerStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        val formSchema = objectMapper.readTree(signUpForm.formSchema)
        val formLabels = mutableListOf<String>()

        if (formSchema.isArray) {
            formSchema.forEach { field ->
                val label = field.get("label")?.asText()
                if (label != null) {
                    formLabels.add(label)
                }
            }
        }

        val headerRow = sheet.createRow(0)
        val baseHeaders = listOf("공간 이름", "시작 시간", "종료 시간", "대여 항목 수", "연락처")
        val allHeaders = baseHeaders + formLabels

        allHeaders.forEachIndexed { idx, header ->
            val cell = headerRow.createCell(idx)
            cell.setCellValue(header)
            cell.cellStyle = headerStyle
        }

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        usageHistories.forEachIndexed { index, history ->
            val row = sheet.createRow(index + 1)

            row.createCell(0).setCellValue(history.spaceName)
            row.createCell(1).setCellValue(history.startAt.format(dateFormatter))
            row.createCell(2).setCellValue(history.endAt?.format(dateFormatter) ?: "사용 중")
            row.createCell(3).setCellValue(history.rentalCount.toDouble())
            row.createCell(4).setCellValue(history.userPhone)

            val formResult = objectMapper.readTree(history.formResult)
            formLabels.forEachIndexed { formIdx, label ->
                val answer = formResult.get(label)?.asText() ?: ""
                row.createCell(baseHeaders.size + formIdx).setCellValue(answer)
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
}
