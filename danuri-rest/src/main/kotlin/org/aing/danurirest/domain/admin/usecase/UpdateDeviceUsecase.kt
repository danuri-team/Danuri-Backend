package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.device.entity.Device
import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.admin.dto.UpdateDeviceRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UpdateDeviceUsecase(
    private val deviceRepository: DeviceRepository,
    private val companyRepository: CompanyRepository,
    private val spaceRepository: SpaceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase
) {
    fun execute(deviceId: UUID, request: UpdateDeviceRequest): DeviceResponse {
        val device = deviceRepository.findById(deviceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }
        
        // 현재 인증된 관리자의 회사 ID 가져오기
        val companyId = getAdminCompanyIdUsecase.execute()
        
        // 디바이스가 관리자의 회사에 속하는지 확인
        if (device.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
        
        val company = companyRepository.findById(companyId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        
        val space = spaceRepository.findById(request.spaceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }
        
        // 공간이 해당 회사에 속하는지 확인
        if (space.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
        
        // 활성 상태 변경을 위한 end_at 설정
        val endAt = if (request.isActive) null else LocalDateTime.now()
        
        val updatedDevice = Device(
            id = device.id,
            company = company,
            space = space,
            role = device.role,
            create_at = device.create_at,
            end_at = endAt
        )
        
        return DeviceResponse.from(deviceRepository.update(updatedDevice))
    }
} 