package org.aing.danurirest.domain.auth.dto

import jakarta.validation.constraints.NotNull

data class TokenRefreshRequest(
    @field:NotNull
    val refreshToken: String,
)
