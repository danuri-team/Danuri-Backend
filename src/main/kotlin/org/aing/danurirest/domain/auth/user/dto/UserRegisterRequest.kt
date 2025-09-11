package org.aing.danurirest.domain.auth.user.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.UUID

data class UserRegisterRequest(
    @field:NotBlank(message = "전화번호는 필수입니다.")
    @field:Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    val phone: String,
    @field:NotNull(message = "회사 ID는 필수입니다.")
    val companyId: UUID,
)
