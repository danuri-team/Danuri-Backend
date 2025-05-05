package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetDeviceUsecase(
    private val deviceRepository: DeviceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    fun execute(deviceId: UUID): DeviceResponse {
        val device = deviceRepository.findById(deviceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }
        
        // 현재 인증된 관리자의 회사 ID 가져오기
        val companyId = getAdminCompanyIdUsecase.execute()
        
        // 디바이스가 관리자의 회사에 속하는지 확인
        if (device.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
        
        return DeviceResponse.from(device)
    }
} 