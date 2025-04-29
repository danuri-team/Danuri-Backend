package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.auth.admin.dto.DeviceResponse
import org.springframework.stereotype.Service

@Service
class GetAllDevicesUsecase(
    private val deviceRepository: DeviceRepository
) {
    fun execute(): List<DeviceResponse> {
        val devices = deviceRepository.findAll()
        return devices.map { DeviceResponse.from(it) }
    }
} 