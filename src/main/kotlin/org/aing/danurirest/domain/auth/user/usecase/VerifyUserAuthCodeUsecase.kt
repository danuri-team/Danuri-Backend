package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthorizationCodeRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.refreshToken.RefreshTokenRepository
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.aing.danurirest.persistence.verify.VerifyCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VerifyUserAuthCodeUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider,
    private val verifyCodeRepository: VerifyCodeRepository,
) {
    @Transactional
    fun execute(request: AuthorizationCodeRequest): SignInResponse {
        verifyCodeRepository.consume(request.authCode) ?: throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)

        val user =
            userJpaRepository
                .findByPhone(request.phone)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        val accessToken =
            jwtProvider.generateToken(
                id = user.id ?: throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR),
                tokenType = TokenType.ACCESS_TOKEN,
                role = Role.ROLE_USER,
            )

        val refreshToken =
            jwtProvider.generateToken(
                id = user.id!!,
                tokenType = TokenType.REFRESH_TOKEN,
                role = Role.ROLE_USER,
            )

        refreshTokenRepository.save(
            userId = user.id!!.toString(),
            refreshToken = refreshToken.token,
        )

        return SignInResponse(accessToken, refreshToken)
    }
}
