package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.aing.danuridomain.persistence.admin.Status
import org.aing.danuridomain.persistence.user.enum.Role
import java.util.UUID

data class AdminUpdateRequest(
    @field:NotNull(message = "회사 ID는 필수 입력값입니다.")
    val companyId: UUID,
    
    @field:NotBlank(message = "이메일은 필수 입력값입니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    @field:Size(max = 100, message = "이메일은 최대 100자까지 입력 가능합니다.")
    val email: String,
    
    @field:NotBlank(message = "전화번호는 필수 입력값입니다.")
    @field:Pattern(
        regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$",
        message = "올바른 전화번호 형식이 아닙니다."
    )
    val phone: String,
    
    @field:NotNull(message = "역할은 필수 입력값입니다.")
    val role: Role,
    
    @field:NotNull(message = "상태는 필수 입력값입니다.")
    val status: Status
) 