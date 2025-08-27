package org.aing.danurirest.domain.admin.dto

data class SignInDeviceResponse(
    val qrLink: String,
    val code: String,
)
