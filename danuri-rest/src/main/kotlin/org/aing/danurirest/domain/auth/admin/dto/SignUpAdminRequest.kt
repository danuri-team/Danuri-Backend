package org.aing.danurirest.domain.auth.admin.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.UUID

data class SignUpAdminRequest(
    @field:NotNull(message = "회사 ID는 필수 입력값입니다.")
    val companyId: UUID,
    @field:NotBlank(message = "전화번호는 필수 입력값입니다.")
    @field:Pattern(
        regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$",
        message = "올바른 전화번호 형식이 아닙니다.",
    )
    val phone: String,
    @field:NotBlank(message = "이메일은 필수 입력값입니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    @field:Size(max = 100, message = "이메일은 최대 100자까지 입력 가능합니다.")
    val email: String,
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8-20자여야 합니다.",
    )
    val password: String,
)
