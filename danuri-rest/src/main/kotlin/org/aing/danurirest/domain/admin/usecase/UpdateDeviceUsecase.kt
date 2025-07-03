package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.admin.dto.UpdateDeviceRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdateDeviceUsecase(
    private val deviceJpaRepository: DeviceJpaRepository,
    private val companyJpaRepository: CompanyJpaRepository,
    private val spaceJpaRepository: SpaceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        deviceId: UUID,
        request: UpdateDeviceRequest,
    ): DeviceResponse {
        val device =
            deviceJpaRepository
                .findById(deviceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }

        val companyId = getAdminCompanyIdUsecase.execute()

        if (device.company.id != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val company =
            companyJpaRepository
                .findById(companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val space =
            spaceJpaRepository
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

        return DeviceResponse.from(deviceJpaRepository.save(updatedDevice))
    }
}
