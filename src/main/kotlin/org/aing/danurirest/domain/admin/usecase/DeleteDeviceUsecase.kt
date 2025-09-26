package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteDeviceUsecase(
    private val deviceJpaRepository: DeviceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(deviceId: UUID) {
        val companyId = getAdminCompanyIdUsecase.execute()

        val device =
            deviceJpaRepository.findByIdAndCompanyId(deviceId, companyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE)

        deviceJpaRepository.delete(device)
    }
}
