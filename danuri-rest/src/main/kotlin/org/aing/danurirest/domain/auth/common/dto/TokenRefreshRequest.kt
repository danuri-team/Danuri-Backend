package org.aing.danurirest.domain.auth.common.dto

import jakarta.validation.constraints.NotNull

data class TokenRefreshRequest(
    @field:NotNull
    val refreshToken: String,
)
