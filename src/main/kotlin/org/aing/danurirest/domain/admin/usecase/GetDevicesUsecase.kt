package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.DeviceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetDevicesUsecase(
    private val deviceJpaRepository: DeviceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(): List<DeviceResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()

        val devices = deviceJpaRepository.findAllByCompanyId(companyId)

        return devices.map { DeviceResponse.from(it) }
    }
}
