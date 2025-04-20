package org.aing.danurirest.domain.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class SignInRequest(
    @field:NotNull
    @Email
    val email: String,
    @field:NotNull
    val password: String,
)
