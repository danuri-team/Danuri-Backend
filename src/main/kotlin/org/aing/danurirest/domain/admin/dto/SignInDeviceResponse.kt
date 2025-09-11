package org.aing.danurirest.domain.admin.dto

import java.time.LocalDateTime

data class SignInDeviceResponse(
    val qrLink: String,
    val code: String,
    val expireAt: LocalDateTime,
)
