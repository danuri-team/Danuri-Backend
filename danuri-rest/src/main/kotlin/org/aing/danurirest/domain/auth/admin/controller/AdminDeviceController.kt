package org.aing.danurirest.domain.auth.admin.controller

import org.aing.danurirest.domain.auth.admin.dto.DeviceResponse
import org.aing.danurirest.domain.auth.admin.dto.DeviceSignInRequest
import org.aing.danurirest.domain.auth.admin.dto.RegisterDeviceRequest
import org.aing.danurirest.domain.auth.admin.dto.SignInResponse
import org.aing.danurirest.domain.auth.admin.dto.UpdateDeviceRequest
import org.aing.danurirest.domain.auth.admin.usecase.DeleteDeviceUsecase
import org.aing.danurirest.domain.auth.admin.usecase.DeviceSignInUsecase
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyDevicesUsecase
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.domain.auth.admin.usecase.GetDeviceUsecase
import org.aing.danurirest.domain.auth.admin.usecase.GetDevicesBySpaceUsecase
import org.aing.danurirest.domain.auth.admin.usecase.RegisterDeviceUsecase
import org.aing.danurirest.domain.auth.admin.usecase.UpdateDeviceUsecase
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
    private val deviceSignInUsecase: DeviceSignInUsecase,
    private val getDeviceUsecase: GetDeviceUsecase,
    private val getAdminCompanyDevicesUsecase: GetAdminCompanyDevicesUsecase,
    private val getDevicesBySpaceUsecase: GetDevicesBySpaceUsecase,
    private val updateDeviceUsecase: UpdateDeviceUsecase,
    private val deleteDeviceUsecase: DeleteDeviceUsecase,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    @PostMapping
    fun registerDevice(
        @RequestBody registerDeviceRequest: RegisterDeviceRequest,
    ): ResponseEntity<Unit> =
        registerDeviceUsecase.execute(registerDeviceRequest).run {
            ResponseEntity.noContent().build()
        }

    @PostMapping("/token")
    fun generateDeviceToken(
        @RequestBody deviceSignInRequest: DeviceSignInRequest,
    ): ResponseEntity<SignInResponse> =
        deviceSignInUsecase.execute(deviceSignInRequest).run {
            ResponseEntity.ok(this)
        }
        
    @GetMapping("/{deviceId}")
    fun getDevice(
        @PathVariable deviceId: UUID,
    ): ResponseEntity<DeviceResponse> = 
        getDeviceUsecase.execute(deviceId).run {
            ResponseEntity.ok(this)
        }
        
    @GetMapping
    fun getAllDevices(): ResponseEntity<List<DeviceResponse>> = 
        getAdminCompanyDevicesUsecase.execute().run {
            ResponseEntity.ok(this)
        }
        
    @GetMapping("/space/{spaceId}")
    fun getDevicesBySpace(
        @PathVariable spaceId: UUID,
    ): ResponseEntity<List<DeviceResponse>> = 
        getDevicesBySpaceUsecase.execute(spaceId).run {
            ResponseEntity.ok(this)
        }
        
    @PutMapping("/{deviceId}")
    fun updateDevice(
        @PathVariable deviceId: UUID,
        @RequestBody updateDeviceRequest: UpdateDeviceRequest,
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
} 