package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.auth.admin.dto.DeviceResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetDevicesBySpaceUsecase(
    private val deviceRepository: DeviceRepository
) {
    fun execute(spaceId: UUID): List<DeviceResponse> {
        val devices = deviceRepository.findBySpaceId(spaceId)
        return devices.map { DeviceResponse.from(it) }
    }
} 