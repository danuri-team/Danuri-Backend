package org.aing.danurirest.domain.auth.admin.usecase

import io.github.bucket4j.Bucket
import jakarta.servlet.http.HttpServletResponse
import org.aing.danurirest.domain.auth.admin.dto.SignInRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.global.util.CookieUtil
import org.aing.danurirest.persistence.admin.Status
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.refreshToken.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class SignInUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
    private val loginBucket: Bucket,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.access-token-expires}")
    private val accessTokenExpires: Long,
    @Value("\${jwt.refresh-token-expires}")
    private val refreshTokenExpires: Long,
) {
    fun execute(
        request: SignInRequest,
        response: HttpServletResponse,
    ) {
        checkRateLimit()

        val admin = findAdminByEmail(request.email)
        validatePassword(admin, request.password)

        if (admin.status == Status.NEED_COMPANY_APPROVE) {
            throw CustomException(CustomErrorCode.NEED_COMPANY_APPROVE)
        }

        val token = generateTokens(admin)

        val accessTokenCookie =
            CookieUtil.createSecureCookie(
                name = "accessToken",
                value = token.accessToken.token,
                maxAge = accessTokenExpires,
            )

        val refreshTokenCookie =
            CookieUtil.createSecureCookie(
                name = "refreshToken",
                value = token.refreshToken?.token ?: "",
                maxAge = refreshTokenExpires,
            )

        response.addHeader("Set-Cookie", accessTokenCookie.toString())
        response.addHeader("Set-Cookie", refreshTokenCookie.toString())
    }

    private fun checkRateLimit() {
        if (!loginBucket.tryConsume(1)) {
            throw CustomException(CustomErrorCode.TOO_MANY_REQUESTS)
        }
    }

    private fun findAdminByEmail(email: String): Admin =
        adminJpaRepository
            .findByEmail(email)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

    private fun validatePassword(
        admin: Admin,
        rawPassword: String,
    ) {
        if (!passwordEncoder.matches(rawPassword, admin.password)) {
            throw CustomException(CustomErrorCode.WRONG_PASSWORD)
        }
    }

    private fun generateTokens(admin: Admin): SignInResponse {
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

        refreshTokenRepository.save(
            userId = admin.id!!.toString(),
            refreshToken = refreshToken.token,
        )

        return SignInResponse(accessToken, refreshToken)
    }
}
