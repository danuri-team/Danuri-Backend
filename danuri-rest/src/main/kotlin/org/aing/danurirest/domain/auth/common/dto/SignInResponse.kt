package org.aing.danurirest.domain.auth.common.dto

import org.aing.danurirest.global.security.jwt.dto.JwtDetails

data class SignInResponse(
    val accessToken: JwtDetails,
    val refreshToken: JwtDetails,
)
