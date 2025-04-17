package org.aing.danurirest.global.security.jwt.dto

import java.util.Date

data class JwtDetails(
    val token: String,
    val expiredAt: Date,
)
