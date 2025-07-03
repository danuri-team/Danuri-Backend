package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import java.util.UUID

data class UserRequest(
    val companyId: UUID? = null,
    @field:NotBlank(message = "이름은 필수 입력값입니다.")
    @field:Size(min = 1, max = 30, message = "이름은 1-30자여야 합니다.")
    val name: String,
    @field:NotNull(message = "성별은 필수 입력값입니다.")
    val sex: Sex,
    @field:NotNull(message = "나이는 필수 입력값입니다.")
    @field:NotNull(message = "연령대는 필수 입력값입니다.")
    val age: Age,
    @field:NotBlank(message = "전화번호는 필수 입력값입니다.")
    @field:Pattern(
        regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$",
        message = "올바른 전화번호 형식이 아닙니다.",
    )
    val phone: String,
)
