package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.admin.dto.RegisterDeviceRequest
import org.aing.danurirest.domain.admin.dto.SignInDeviceResponse
import org.aing.danurirest.domain.admin.dto.UpdateDeviceRequest
import org.aing.danurirest.domain.admin.usecase.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/devices")
class AdminDeviceController(
    private val registerDeviceUsecase: RegisterDeviceUsecase,
    private val getDeviceUsecase: GetDeviceUsecase,
    private val getAdminCompanyDevicesUsecase: GetDevicesUsecase,
    private val updateDeviceUsecase: UpdateDeviceUsecase,
    private val deleteDeviceUsecase: DeleteDeviceUsecase,
    private val signInDeviceUsecase: SignInDeviceUsecase,
) {
    @PostMapping
    fun registerDevice(
        @Valid @RequestBody registerDeviceRequest: RegisterDeviceRequest,
    ): ResponseEntity<Unit> =
        registerDeviceUsecase.execute(registerDeviceRequest).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping("/{deviceId}")
    fun getDevice(
        @PathVariable deviceId: UUID,
    ): ResponseEntity<DeviceResponse> =
        getDeviceUsecase.execute(deviceId).run {
            ResponseEntity.ok(this)
        }

    @GetMapping
    fun getAllDevices(
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<DeviceResponse>> =
        getAdminCompanyDevicesUsecase.execute(pageable).run {
            ResponseEntity.ok(this)
        }

    @PutMapping("/{deviceId}")
    fun updateDevice(
        @PathVariable deviceId: UUID,
        @Valid @RequestBody updateDeviceRequest: UpdateDeviceRequest,
    ): ResponseEntity<DeviceResponse> =
        updateDeviceUsecase.execute(deviceId, updateDeviceRequest).run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping("/{deviceId}")
    fun deleteDevice(
        @PathVariable deviceId: UUID,
    ): ResponseEntity<Unit> =
        deleteDeviceUsecase.execute(deviceId).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping("/{deviceId}/sign-in")
    fun signInDevice(
        @PathVariable deviceId: UUID,
    ): ResponseEntity<SignInDeviceResponse> =
        signInDeviceUsecase.execute(deviceId).run {
            ResponseEntity.ok(this)
        }
}
