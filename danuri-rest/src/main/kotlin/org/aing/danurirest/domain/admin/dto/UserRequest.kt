package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.aing.danuridomain.persistence.user.enum.Sex
import java.util.UUID

data class UserRequest(
    val companyId: UUID? = null,
    
    @field:NotBlank(message = "이름은 필수 입력값입니다.")
    @field:Size(min = 1, max = 30, message = "이름은 1-30자여야 합니다.")
    val name: String,
    
    @field:NotNull(message = "성별은 필수 입력값입니다.")
    val sex: Sex,
    
    @field:NotNull(message = "나이는 필수 입력값입니다.")
    @field:Min(value = 1, message = "나이는 최소 1세 이상이어야 합니다.")
    @field:Max(value = 150, message = "나이는 최대 150세 이하여야 합니다.")
    val age: Int,
    
    @field:NotBlank(message = "전화번호는 필수 입력값입니다.")
    @field:Pattern(regexp = "^[0-9]{10,11}$", message = "올바른 전화번호 형식이 아닙니다. (10-11자리 숫자)")
    val phone: String
) 