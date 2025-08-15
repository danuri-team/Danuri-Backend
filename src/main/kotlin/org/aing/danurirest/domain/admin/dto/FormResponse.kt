package org.aing.danurirest.domain.admin.dto

import org.aing.danurirest.persistence.form.entity.Form
import java.util.UUID

data class FormResponse(
    val id: UUID,
    val title: String,
    val schema: String,
    val companyId: UUID,
) {
    companion object {
        fun from(entity: Form): FormResponse =
            FormResponse(
                id = entity.id!!,
                title = entity.title,
                schema = entity.schema,
                companyId = entity.company.id!!,
            )
    }
}
