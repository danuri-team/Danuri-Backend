package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.springframework.stereotype.Service

@Service
class GetAdminCompanyDevicesUsecase(
    private val deviceRepository: DeviceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    fun execute(): List<DeviceResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()

        val devices = deviceRepository.findByCompanyId(companyId)
        
        return devices.map { DeviceResponse.from(it) }
    }
} 