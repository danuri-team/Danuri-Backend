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
import java.util.UUID

@Service
class UpdateDeviceUsecase(
    private val deviceRepository: DeviceRepository,
    private val companyRepository: CompanyRepository,
    private val spaceRepository: SpaceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        deviceId: UUID,
        request: UpdateDeviceRequest,
    ): DeviceResponse {
        val device =
            deviceRepository
                .findById(deviceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }

        val companyId = getAdminCompanyIdUsecase.execute()

        if (device.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val company =
            companyRepository
                .findById(companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val space =
            spaceRepository
                .findById(request.spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        if (space.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val updatedDevice =
            Device(
                id = device.id,
                company = company,
                role = device.role,
            )

        return DeviceResponse.from(deviceRepository.save(updatedDevice))
    }
}
