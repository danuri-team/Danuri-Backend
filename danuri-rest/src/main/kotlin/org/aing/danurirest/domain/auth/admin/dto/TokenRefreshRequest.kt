package org.aing.danurirest.domain.auth.admin.dto

import jakarta.validation.constraints.NotNull

data class TokenRefreshRequest(
    @field:NotNull
    val refreshToken: String,
)
