package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.RegisterDeviceRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.aing.danurirest.persistence.user.Role
import org.springframework.stereotype.Service

@Service
class RegisterDeviceUsecase(
    private val deviceJpaRepository: DeviceJpaRepository,
    private val companyJpaRepository: CompanyJpaRepository,
    private val spaceJpaRepository: SpaceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(registerDeviceRequest: RegisterDeviceRequest) {
        if (deviceJpaRepository.findById(registerDeviceRequest.deviceId).isPresent) {
            throw CustomException(CustomErrorCode.DEVICE_ALREADY_REGISTERED)
        }

        val companyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyJpaRepository.findById(companyId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY)
            }

        val space =
            spaceJpaRepository.findById(registerDeviceRequest.spaceId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND_SPACE)
            }

        if (space.company.id != company.id) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        deviceJpaRepository.save(
            Device(
                company = company,
                role = Role.ROLE_DEVICE,
            ),
        )
    }
}
