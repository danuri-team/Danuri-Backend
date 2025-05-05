package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetDevicesByCompanyUsecase(
    private val deviceRepository: DeviceRepository
) {
    fun execute(companyId: UUID): List<DeviceResponse> {
        val devices = deviceRepository.findByCompanyId(companyId)
        return devices.map { DeviceResponse.from(it) }
    }
} 