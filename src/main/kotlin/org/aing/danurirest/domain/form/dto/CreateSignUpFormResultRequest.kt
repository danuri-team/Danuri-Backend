package org.aing.danurirest.domain.form.dto

import jakarta.validation.constraints.NotBlank

data class CreateSignUpFormResultRequest(
    @field:NotBlank(message = "결과는 필수 입력값입니다.")
    val result: String,
)
