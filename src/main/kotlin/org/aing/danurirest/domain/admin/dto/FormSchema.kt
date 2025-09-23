package org.aing.danurirest.domain.admin.dto

import org.aing.danurirest.domain.admin.dto.enum.FormType

data class FormSchema(
    val id: Int,
    val key: String,
    val type: FormType,
    val label: String,
    val options: List<FormSchemaOptions>,
    val placeholder: String?,
    val isRequired: Boolean,
    val isMultiSelect: Boolean,
)
