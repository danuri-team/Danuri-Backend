package org.aing.danurirest.domain.auth.device.usecase

import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.domain.auth.device.dto.DeviceSignInRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.springframework.stereotype.Service

@Service
class DeviceSignInUsecase(
    private val jwtProvider: JwtProvider,
    private val deviceJpaRepository: DeviceJpaRepository,
) {
    fun execute(request: DeviceSignInRequest): SignInResponse {
        val device: Device =
            deviceJpaRepository
                .findById(
                    request.deviceId,
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
