package org.aing.danurirest.domain.admin.service.excel

import org.aing.danurirest.domain.admin.service.excel.config.ExcelProperties
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component

/**
 * Excel 스타일 생성 및 관리를 담당하는 컴포넌트
 */
@Component
class ExcelStyleProvider(
    private val properties: ExcelProperties,
) {
    /**
     * 헤더 행에 사용할 스타일 생성
     */
    fun createHeaderStyle(workbook: Workbook): CellStyle =
        workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            borderTop = BorderStyle.THIN
            borderBottom = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN

            setFont(
                workbook.createFont().apply {
                    bold = true
                    fontName = properties.style.fontName
                    fontHeightInPoints = properties.style.fontSize.toShort()
                },
            )
        }

    /**
     * 빨간색 폰트 스타일 생성 (일요일, 휴관일 등)
     */
    fun createRedFontStyle(workbook: XSSFWorkbook): CellStyle =
        createColoredFontStyle(
            workbook,
            byteArrayOf(255.toByte(), 0, 0),
        )

    /**
     * 파란색 폰트 스타일 생성 (토요일 등)
     */
    fun createBlueFontStyle(workbook: XSSFWorkbook): CellStyle =
        createColoredFontStyle(
            workbook,
            byteArrayOf(0, 0, 255.toByte()),
        )

    private fun createColoredFontStyle(
        workbook: XSSFWorkbook,
        rgb: ByteArray,
    ): CellStyle =
        workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            borderTop = BorderStyle.THIN
            borderBottom = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN

            setFont(
                workbook.createFont().apply {
                    setColor(XSSFColor(rgb, null))
                    fontName = properties.style.fontName
                    fontHeightInPoints = properties.style.fontSize.toShort()
                },
            )
        }
}
