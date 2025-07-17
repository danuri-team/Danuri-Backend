package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class DeleteHelpRequest(
    val isResolved: Boolean = true,
    @field:NotNull(message = "도움 ID 여부는 필수 입력값입니다.")
    val helpId: UUID,
)
