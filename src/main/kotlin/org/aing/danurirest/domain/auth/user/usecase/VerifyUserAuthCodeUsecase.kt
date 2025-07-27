package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthorizationCodeRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.repository.UserAuthCodeJpaRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class VerifyUserAuthCodeUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val userAuthCodeJpaRepository: UserAuthCodeJpaRepository,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun execute(request: AuthorizationCodeRequest): SignInResponse {
        val userAuthCodes = userAuthCodeJpaRepository.findByPhone(request.phone)

        if (userAuthCodes.isEmpty()) {
            throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)
        }

        // 만료된 코드들 먼저 삭제
        val now = LocalDateTime.now()
        val expiredCodes = userAuthCodes.filter { now.isAfter(it.expiredAt) }
        if (expiredCodes.isNotEmpty()) {
            expiredCodes.forEach { userAuthCodeJpaRepository.delete(it) }
        }

        // 만료되지 않은 코드 중에서 일치하는 것 찾기
        userAuthCodes
            .filter { !now.isAfter(it.expiredAt) }
            .find { it.authCode == request.authCode }
            ?: throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)

        // 인증 성공 후 해당 전화번호의 모든 인증코드 삭제
        userAuthCodeJpaRepository.deleteByPhone(request.phone)

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

        return SignInResponse(accessToken, refreshToken)
    }
}
