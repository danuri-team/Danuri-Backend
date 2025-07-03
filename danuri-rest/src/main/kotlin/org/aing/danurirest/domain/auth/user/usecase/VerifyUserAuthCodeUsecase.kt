package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.repository.UserAuthCodeRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VerifyUserAuthCodeUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val userAuthCodeRepository: UserAuthCodeRepository,
    private val jwtProvider: JwtProvider,
) {
    fun execute(
        phone: String,
        authCode: String,
    ): SignInResponse {
        val userAuthCode =
            userAuthCodeRepository
                .findByPhone(phone)
                .orElseThrow { CustomException(CustomErrorCode.INVALID_AUTH_CODE) }

        if (LocalDateTime.now().isAfter(userAuthCode.expiredAt)) {
            userAuthCodeRepository.deleteByPhone(phone)
            throw CustomException(CustomErrorCode.EXPIRED_AUTH_CODE)
        }

        if (userAuthCode.authCode != authCode) {
            throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)
        }

        userAuthCodeRepository.deleteByPhone(phone)

        val user =
            userJpaRepository
                .findByPhone(phone)
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

        return SignInResponse(accessToken, refreshToken)
    }
}
