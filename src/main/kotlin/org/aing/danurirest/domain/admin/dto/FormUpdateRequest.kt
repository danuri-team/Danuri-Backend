package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank

data class FormUpdateRequest(
    @field:NotBlank(message = "제목은 필수 입력값입니다.")
    val title: String,

    @field:NotBlank(message = "JSON 스키마는 필수 입력값입니다.")
    val schema: String,
)
