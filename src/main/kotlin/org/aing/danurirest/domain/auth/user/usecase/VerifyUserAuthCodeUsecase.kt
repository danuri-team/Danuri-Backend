package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthorizationCodeRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.refreshToken.entity.RefreshToken
import org.aing.danurirest.persistence.refreshToken.repository.RefreshTokenRepository
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.aing.danurirest.persistence.verify.repository.VerifyCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VerifyUserAuthCodeUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val verifyCodeRepository: VerifyCodeRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun execute(request: AuthorizationCodeRequest): SignInResponse {
        val userAuthCode =
            verifyCodeRepository.findById(request.authCode).orElseThrow { CustomException(CustomErrorCode.INVALID_AUTH_CODE) }

        if (userAuthCode.phoneNumber != request.phone) {
            throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)
        }

        verifyCodeRepository.delete(userAuthCode)

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

        refreshTokenRepository.save(RefreshToken(user.id!!, refreshToken.token))

        return SignInResponse(accessToken, refreshToken)
    }
}
