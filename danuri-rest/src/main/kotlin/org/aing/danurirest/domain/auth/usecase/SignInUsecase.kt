package org.aing.danurirest.domain.auth.usecase

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.auth.dto.SignInRequest
import org.aing.danurirest.domain.auth.dto.SignInResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class SignInUsecase(
    private val adminRepository: AdminRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
) {
    fun execute(request: SignInRequest): SignInResponse {
        val user: Admin = adminRepository.findByEmail(request.email).orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw CustomException(CustomErrorCode.WRONG_PASSWORD)
        }

        val accessToken =
            jwtProvider.generateToken(
                user.id!!,
                TokenType.ACCESS_TOKEN,
                user.role,
            )
        val refreshToken =
            jwtProvider.generateToken(
                user.id!!,
                TokenType.REFRESH_TOKEN,
                user.role,
            )

        return SignInResponse(accessToken, refreshToken)
    }
}
