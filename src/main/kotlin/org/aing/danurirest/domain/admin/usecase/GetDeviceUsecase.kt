package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetDeviceUsecase(
    private val deviceJpaRepository: DeviceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    fun execute(deviceId: UUID): DeviceResponse {
        val device = deviceJpaRepository.findById(deviceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }

        val companyId = getAdminCompanyIdUsecase.execute()

        if (device.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
        
        return DeviceResponse.from(device)
    }
} 