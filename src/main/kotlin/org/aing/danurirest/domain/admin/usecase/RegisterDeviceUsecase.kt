package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.RegisterDeviceRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.user.Role
import org.springframework.stereotype.Service

@Service
class RegisterDeviceUsecase(
    private val deviceJpaRepository: DeviceJpaRepository,
    private val companyJpaRepository: CompanyJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(registerDeviceRequest: RegisterDeviceRequest) {
        val companyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyJpaRepository.findById(companyId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY)
            }

        deviceJpaRepository.save(
            Device(
                company = company,
                name = registerDeviceRequest.name,
                role = Role.ROLE_DEVICE,
            ),
        )
    }
}
