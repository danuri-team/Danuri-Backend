package org.aing.danurirest.domain.auth.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.device.entity.Device
import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danuridomain.persistence.user.enum.Role
import org.aing.danurirest.domain.auth.dto.RegisterDeviceRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service

@Service
class RegisterDeviceUsecase(
    private val deviceRepository: DeviceRepository,
    private val companyRepository: CompanyRepository,
    private val spaceRepository: SpaceRepository,
) {
    fun execute(registerDeviceRequest: RegisterDeviceRequest) {
        if (deviceRepository.findByDeviceId(registerDeviceRequest.deviceId).isPresent) {
            throw CustomException(CustomErrorCode.DEVICE_ALREADY_REGISTERED)
        }

        val company =
            companyRepository.findById(registerDeviceRequest.companyId).orElseThrow {
                throw CustomException(CustomErrorCode.VALIDATION_ERROR)
            }

        val space =
            spaceRepository.findById(registerDeviceRequest.spaceId).orElseThrow {
                throw CustomException(CustomErrorCode.VALIDATION_ERROR)
            }

        deviceRepository.save(
            Device(
                company = company,
                role = Role.ROLE_DEVICE,
                space = space,
            ),
        )
    }
}
