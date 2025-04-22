package org.aing.danurirest.domain.auth.admin.usecase

import io.jsonwebtoken.Claims
import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.auth.admin.dto.SignInResponse
import org.aing.danurirest.domain.auth.admin.dto.TokenRefreshRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TokenRefreshUsecase(
    private val jwtProvider: JwtProvider,
    private val adminRepository: AdminRepository,
) {
    fun execute(request: TokenRefreshRequest): SignInResponse {
        val claims: Claims =
            jwtProvider.getPayload(
                token = request.refreshToken,
                tokenType = TokenType.REFRESH_TOKEN,
            )

        val admin: Admin =
            adminRepository.findByID(UUID.fromString(claims.subject)).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND_USER)
            }

        val accessToken =
            jwtProvider.generateToken(
                admin.id!!,
                TokenType.ACCESS_TOKEN,
                admin.role,
            )
        val refreshToken =
            jwtProvider.generateToken(
                admin.id!!,
                TokenType.REFRESH_TOKEN,
                admin.role,
            )

        return SignInResponse(accessToken, refreshToken)
    }
}
