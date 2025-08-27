package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthorizationCodeRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.repository.UserAuthCodeJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class IssuePasswordResetTokenUsecase(
    private val userAuthCodeJpaRepository: UserAuthCodeJpaRepository,
    private val jwtProvider: JwtProvider,
    private val adminJpaRepository: AdminJpaRepository,
) {
    @Transactional
    fun execute(request: AuthorizationCodeRequest): SignInResponse {
        val userAuthCodes = userAuthCodeJpaRepository.findByPhone(request.phone)

        if (userAuthCodes.isEmpty()) {
            throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)
        }

        // 만료된 인증코드들 삭제
        val expiredCodes = userAuthCodes.filter { LocalDateTime.now().isAfter(it.expiredAt) }
        if (expiredCodes.isNotEmpty()) {
            expiredCodes.forEach { userAuthCodeJpaRepository.delete(it) }
        }

        // 유효한 인증코드들 중에서 요청된 코드와 일치하는 것 찾기
        userAuthCodes
            .filter { LocalDateTime.now().isBefore(it.expiredAt) || LocalDateTime.now().isEqual(it.expiredAt) }
            .find { it.authCode == request.authCode }
            ?: throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)

        // 사용된 인증코드 삭제 (또는 해당 전화번호의 모든 인증코드 삭제)
        userAuthCodeJpaRepository.deleteByPhone(request.phone)

        val user =
            adminJpaRepository
                .findByPhone(request.phone)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        val accessToken =
            jwtProvider.generateToken(
                id = user.id ?: throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR),
                tokenType = TokenType.ACCESS_TOKEN,
                role = Role.ROLE_ADMIN,
                expireTime = 5 * 60 * 1000L,
            )

        return SignInResponse(
            accessToken = accessToken,
            refreshToken = null,
        )
    }
}
