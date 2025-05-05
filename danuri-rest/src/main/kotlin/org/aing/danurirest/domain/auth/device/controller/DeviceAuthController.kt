package org.aing.danurirest.domain.auth.device.controller

import org.aing.danurirest.domain.auth.device.dto.DeviceSignInRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.domain.auth.admin.usecase.DeviceSignInUsecase
import org.aing.danurirest.domain.auth.device.dto.DeviceSpaceInfoResponse
import org.aing.danurirest.domain.auth.device.usecase.FetchCurrentDeviceSpaceUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/device")
class DeviceAuthController(
    private val fetchCurrentDeviceSpaceUsecase: FetchCurrentDeviceSpaceUsecase,
    private val deviceSignInUsecase: DeviceSignInUsecase,
) {
    @GetMapping("/space")
    fun getCurrentDeviceSpace(): ResponseEntity<DeviceSpaceInfoResponse> =
        fetchCurrentDeviceSpaceUsecase.execute().run {
            ResponseEntity.ok(this)
        }

    @PostMapping("/token")
    fun generateDeviceToken(
        @RequestBody deviceSignInRequest: DeviceSignInRequest,
    ): ResponseEntity<SignInResponse> =
        deviceSignInUsecase.execute(deviceSignInRequest).run {
            ResponseEntity.ok(this)
        }
} 