package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.auth.admin.dto.DeviceResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetDeviceUsecase(
    private val deviceRepository: DeviceRepository
) {
    fun execute(deviceId: UUID): DeviceResponse {
        val device = deviceRepository.findById(deviceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }
        
        return DeviceResponse.from(device)
    }
} 