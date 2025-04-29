package org.aing.danurirest.domain.auth.device.controller

import org.aing.danurirest.domain.auth.device.dto.DeviceSpaceInfoResponse
import org.aing.danurirest.domain.auth.device.usecase.FetchCurrentDeviceSpaceUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/device")
class DeviceAuthController(
    private val fetchCurrentDeviceSpaceUsecase: FetchCurrentDeviceSpaceUsecase
) {
    @GetMapping("/space")
    fun getCurrentDeviceSpace(): ResponseEntity<DeviceSpaceInfoResponse> =
        fetchCurrentDeviceSpaceUsecase.execute().run {
            ResponseEntity.ok(this)
        }
} 