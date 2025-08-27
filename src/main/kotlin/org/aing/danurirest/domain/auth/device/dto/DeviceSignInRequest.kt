package org.aing.danurirest.domain.auth.device.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class DeviceSignInRequest(
    @field:NotBlank
    @field:Size(min = 6, max = 64)
    val code: String,
)
