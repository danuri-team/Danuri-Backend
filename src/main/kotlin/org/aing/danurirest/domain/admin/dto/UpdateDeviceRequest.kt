package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank

data class UpdateDeviceRequest(
    @field:NotBlank(message = "별명은 공백일 수 없습니다.")
    val name: String,
)
