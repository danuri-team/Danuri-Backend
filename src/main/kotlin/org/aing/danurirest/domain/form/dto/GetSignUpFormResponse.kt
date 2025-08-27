package org.aing.danurirest.domain.form.dto

import org.aing.danurirest.persistence.form.entity.Form
import java.util.UUID

data class GetSignUpFormResponse(
    val id: UUID,
    val title: String,
    val schema: String,
) {
    companion object {
        fun from(entity: Form) =
            GetSignUpFormResponse(
                id = entity.id!!,
                title = entity.title,
                schema = entity.formSchema,
            )
    }
}
