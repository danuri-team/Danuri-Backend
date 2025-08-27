package org.aing.danurirest.domain.auth.device.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.domain.auth.device.dto.DeviceSignInRequest
import org.aing.danurirest.domain.auth.device.usecase.DeviceSignInUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/device")
class DeviceAuthController(
    private val deviceSignInUsecase: DeviceSignInUsecase,
) {
    @PostMapping("/token")
    fun generateDeviceToken(
        @Valid @RequestBody deviceSignInRequest: DeviceSignInRequest,
    ): ResponseEntity<SignInResponse> =
        deviceSignInUsecase.execute(deviceSignInRequest).run {
            ResponseEntity.ok(this)
        }
}
