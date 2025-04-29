package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.device.entity.Device
import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.auth.admin.dto.DeviceResponse
import org.aing.danurirest.domain.auth.admin.dto.UpdateDeviceRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UpdateDeviceUsecase(
    private val deviceRepository: DeviceRepository,
    private val companyRepository: CompanyRepository,
    private val spaceRepository: SpaceRepository
) {
    fun execute(deviceId: UUID, request: UpdateDeviceRequest): DeviceResponse {
        val device = deviceRepository.findById(deviceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }
        
        val company = companyRepository.findById(request.companyId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        
        val space = spaceRepository.findById(request.spaceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }
        
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