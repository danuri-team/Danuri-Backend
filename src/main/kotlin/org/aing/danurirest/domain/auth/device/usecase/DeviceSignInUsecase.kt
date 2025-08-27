package org.aing.danurirest.domain.auth.device.usecase

import io.github.bucket4j.Bucket
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.domain.auth.device.dto.DeviceSignInRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.device.repository.VerificationCodeRepository
import org.springframework.stereotype.Service

@Service
class DeviceSignInUsecase(
    private val jwtProvider: JwtProvider,
    private val deviceJpaRepository: DeviceJpaRepository,
    private val verificationCodeRepository: VerificationCodeRepository,
    private val loginBucket: Bucket,
) {
    fun execute(request: DeviceSignInRequest): SignInResponse {
        if (!loginBucket.tryConsume(1L)) {
            throw CustomException(CustomErrorCode.TOO_MANY_REQUESTS)
        }

        val deviceVerifyCode =
            verificationCodeRepository.findByCode(request.code)
                ?: throw CustomException(CustomErrorCode.INVALID_AUTH_CODE)

        deviceVerifyCode.deviceId

        val device: Device =
            deviceJpaRepository
                .findById(
                    deviceVerifyCode.deviceId,
                ).orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }

        val accessToken =
            jwtProvider.generateToken(
                device.id!!,
                TokenType.ACCESS_TOKEN,
                device.role,
            )
        val refreshToken =
            jwtProvider.generateToken(
                device.id!!,
                TokenType.REFRESH_TOKEN,
                device.role,
            )

        return SignInResponse(accessToken, refreshToken)
    }
}
