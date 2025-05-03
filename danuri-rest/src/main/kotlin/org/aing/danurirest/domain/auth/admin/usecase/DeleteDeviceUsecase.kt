package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteDeviceUsecase(
    private val deviceRepository: DeviceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    fun execute(deviceId: UUID) {
        val device = deviceRepository.findById(deviceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }
            
        val companyId = getAdminCompanyIdUsecase.execute()
        
        // 디바이스가 관리자의 회사에 속하는지 확인
        if (device.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
            
        deviceRepository.delete(deviceId)
    }
}
