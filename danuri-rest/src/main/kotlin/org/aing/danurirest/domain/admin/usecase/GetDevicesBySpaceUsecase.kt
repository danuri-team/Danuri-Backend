package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetDevicesBySpaceUsecase(
    private val deviceRepository: DeviceRepository,
    private val spaceRepository: SpaceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    fun execute(spaceId: UUID): List<DeviceResponse> {
        // 현재 인증된 관리자의 회사 ID 가져오기
        val companyId = getAdminCompanyIdUsecase.execute()
        
        // 공간 정보 조회
        val space = spaceRepository.findById(spaceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }
        
        // 공간이 관리자의 회사에 속하는지 확인
        if (space.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
        
        // 해당 공간의 디바이스 목록 조회
        val devices = deviceRepository.findBySpaceId(spaceId)
        
        return devices.map { DeviceResponse.from(it) }
    }
} 