package org.aing.danurirest.domain.auth.common.usecase

import io.jsonwebtoken.Claims
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.admin.Status
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.refreshToken.RefreshTokenRepository
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TokenRefreshUsecase(
    private val jwtProvider: JwtProvider,
    private val adminJpaRepository: AdminJpaRepository,
    private val deviceJpaRepository: DeviceJpaRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userJpaRepository: UserJpaRepository,
) {
    fun execute(
        refreshToken: String?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): SignInResponse {
        val token = refreshToken ?: request.cookies?.firstOrNull { it.name == "refreshToken" }?.value

        if (token.isNullOrBlank()) {
            throw CustomException(CustomErrorCode.MISSING_TOKEN)
        }

        val claims: Claims =
            jwtProvider.getPayload(
                token = token,
                tokenType = TokenType.REFRESH_TOKEN,
            )

        val role = Role.valueOf(claims["role"] as String)
        val userId = UUID.fromString(claims.subject)

        refreshTokenRepository.consume(token)
            ?: throw CustomException(CustomErrorCode.INVALID_REFRESH_TOKEN)

        when (role) {
            Role.ROLE_ADMIN -> {
                val admin =
                    adminJpaRepository.findById(userId).orElseThrow {
                        CustomException(CustomErrorCode.NOT_FOUND_USER)
                    }
                if (admin.status == Status.NEED_COMPANY_APPROVE) {
                    throw CustomException(CustomErrorCode.NEED_COMPANY_APPROVE)
                }
            }

            Role.ROLE_DEVICE ->
                deviceJpaRepository.findById(userId).orElseThrow {
                    CustomException(CustomErrorCode.NOT_FOUND_USER)
                }

            Role.ROLE_USER ->
                userJpaRepository.findById(userId).orElseThrow {
                    CustomException(CustomErrorCode.NOT_FOUND_USER)
                }
        }

        val newAccessToken = jwtProvider.generateToken(userId, TokenType.ACCESS_TOKEN, role)
        val newRefreshToken = jwtProvider.generateToken(userId, TokenType.REFRESH_TOKEN, role)

        refreshTokenRepository.save(
            userId = userId.toString(),
            refreshToken = newRefreshToken.token,
        )

        val accessTokenCookie =
            Cookie("accessToken", newAccessToken.token).apply {
                isHttpOnly = true
                secure = true
                path = "/"
                maxAge = 3600
                setAttribute("SameSite", "Strict")
            }

        val refreshTokenCookie =
            Cookie("refreshToken", newRefreshToken.token).apply {
                isHttpOnly = true
                secure = true
                path = "/"
                maxAge = 604800
                setAttribute("SameSite", "Strict")
            }

        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)

        return SignInResponse(newAccessToken, newRefreshToken)
    }
}
