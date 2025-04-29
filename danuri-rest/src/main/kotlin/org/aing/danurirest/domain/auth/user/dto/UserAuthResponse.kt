package org.aing.danurirest.domain.auth.user.dto

import org.aing.danurirest.global.security.jwt.dto.JwtDetails
import java.util.Date

data class UserAuthResponse(
    val accessToken: String,
    val accessTokenExpiredAt: Date,
    val refreshToken: String,
    val refreshTokenExpiredAt: Date,
) {
    companion object {
        fun from(
            accessTokenDetails: JwtDetails,
            refreshTokenDetails: JwtDetails,
        ): UserAuthResponse =
            UserAuthResponse(
                accessToken = accessTokenDetails.token,
                accessTokenExpiredAt = accessTokenDetails.expiredAt,
                refreshToken = refreshTokenDetails.token,
                refreshTokenExpiredAt = refreshTokenDetails.expiredAt,
            )
    }
} 