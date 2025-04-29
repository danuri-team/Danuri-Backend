package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danuridomain.persistence.user.enum.Role
import org.aing.danuridomain.persistence.user.repository.UserAuthCodeRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.dto.JwtDetails
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VerifyUserAuthCodeUsecase(
    private val userRepository: UserRepository,
    private val userAuthCodeRepository: UserAuthCodeRepository,
    private val jwtProvider: JwtProvider,
) {
    fun execute(
        phone: String,
        authCode: String,
    ): Pair<JwtDetails, JwtDetails> {
        // 인증 코드 조회
        val userAuthCode =
            userAuthCodeRepository
                .findByPhone(phone)
                .orElseThrow { CustomException(CustomErrorCode.INVALID_AUTH_CODE) }

        // 인증 코드 만료 확인
        if (LocalDateTime.now().isAfter(userAuthCode.expiredAt)) {
            userAuthCodeRepository.deleteByPhone(phone)
            throw CustomException(CustomErrorCode.EXPIRED_AUTH_CODE)
        }

        // 인증 코드 검증
        if (userAuthCode.authCode != authCode) {
            throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)
        }

        // 인증 성공 후 인증 코드 삭제
        userAuthCodeRepository.deleteByPhone(phone)

        // 해당 전화번호로 사용자 조회
        val user =
            userRepository
                .findByPhone(phone)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        // 사용자 ID로 토큰 발급
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

        return Pair(accessToken, refreshToken)
    }
} 
