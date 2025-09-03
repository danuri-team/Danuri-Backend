package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthorizationCodeRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.verify.repository.VerifyCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssuePasswordResetTokenUsecase(
    private val verifyCodeRepository: VerifyCodeRepository,
    private val jwtProvider: JwtProvider,
    private val adminJpaRepository: AdminJpaRepository,
) {
    @Transactional
    fun execute(request: AuthorizationCodeRequest): SignInResponse {
        val userAuthCode =
            verifyCodeRepository.findByPhoneNumber(request.phone)
                ?: throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)

        verifyCodeRepository.delete(userAuthCode)

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
