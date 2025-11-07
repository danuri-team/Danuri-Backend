package org.aing.danurirest.domain.admin.service.excel.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "excel")
data class ExcelProperties(
    val template: TemplateConfig,
    val data: DataConfig,
    val style: StyleConfig,
    val format: FormatConfig,
    val form: FormConfig,
) {
    data class TemplateConfig(
        val path: String,
    )

    data class DataConfig(
        val startRow: Int,
        val summaryRowOffset: Int,
    )

    data class StyleConfig(
        val fontName: String,
        val fontSize: Int,
    )

    data class FormatConfig(
        val dateTime: String,
    )

    data class FormConfig(
        val ageField: String,
        val sexField: String,
    )
}
