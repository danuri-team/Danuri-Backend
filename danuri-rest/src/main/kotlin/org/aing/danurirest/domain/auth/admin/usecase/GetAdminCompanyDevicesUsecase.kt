package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.auth.admin.dto.DeviceResponse
import org.springframework.stereotype.Service

@Service
class GetAdminCompanyDevicesUsecase(
    private val deviceRepository: DeviceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    fun execute(): List<DeviceResponse> {
        // 현재 인증된 관리자의 회사 ID 가져오기
        val companyId = getAdminCompanyIdUsecase.execute()
        
        // 해당 회사의 디바이스 목록 조회
        val devices = deviceRepository.findByCompanyId(companyId)
        
        return devices.map { DeviceResponse.from(it) }
    }
} 