package org.aing.danurirest.global.security.serivce

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danuridomain.persistence.device.entity.Device
import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthDetailService(
    private val adminRepository: AdminRepository,
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository,
) {
    fun loadAdminByToken(id: UUID): Admin =
        adminRepository.findById(id).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

    fun loadDeviceByToken(id: UUID): Device =
        deviceRepository.findByDeviceId(id).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

    fun loadUserByToken(id: UUID): User =
        userRepository.findById(id).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }
}
