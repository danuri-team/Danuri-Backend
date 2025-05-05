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
        val companyId = getAdminCompanyIdUsecase.execute()
        
        val space = spaceRepository.findById(spaceId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }
        
        if (space.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
        
        val devices = deviceRepository.findBySpaceId(spaceId)
        
        return devices.map { DeviceResponse.from(it) }
    }
} 