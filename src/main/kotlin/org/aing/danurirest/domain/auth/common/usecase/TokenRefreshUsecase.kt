package org.aing.danurirest.domain.auth.common.usecase

import io.jsonwebtoken.Claims
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
    fun execute(refreshToken: String): SignInResponse {
        val claims: Claims =
            jwtProvider.getPayload(
                token = refreshToken,
                tokenType = TokenType.REFRESH_TOKEN,
            )

        val role = Role.valueOf(claims["role"] as String)
        val userId = UUID.fromString(claims.subject)

        refreshTokenRepository.consume(refreshToken)
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

        return SignInResponse(newAccessToken, newRefreshToken)
    }
}
