package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.admin.dto.UpdateDeviceRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateDeviceUsecase(
    private val deviceJpaRepository: DeviceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional
    fun execute(
        deviceId: UUID,
        request: UpdateDeviceRequest,
    ): DeviceResponse {
        val companyId = getAdminCompanyIdUsecase.execute()

        val device =
            deviceJpaRepository.findByIdAndCompanyId(deviceId, companyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE)

        device.name = request.name

        return DeviceResponse.from(deviceJpaRepository.save(device))
    }
}
