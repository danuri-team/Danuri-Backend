package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.util.UUID

data class UserRequest(
    val companyId: UUID? = null,
    @field:NotBlank(message = "전화번호는 필수 입력값입니다.")
    @field:Pattern(
        regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$",
        message = "올바른 전화번호 형식이 아닙니다.",
    )
    val phone: String,
)
