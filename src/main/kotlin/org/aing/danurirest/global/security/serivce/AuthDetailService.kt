package org.aing.danurirest.global.security.serivce

import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.user.entity.User
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthDetailService(
    private val adminJpaRepository: AdminJpaRepository,
    private val deviceJpaRepository: DeviceJpaRepository,
    private val userJpaRepository: UserJpaRepository,
) {
    fun loadAdminByToken(id: UUID): Admin =
        adminJpaRepository.findById(id).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

    fun loadDeviceByToken(id: UUID): Device =
        deviceJpaRepository.findById(id).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

    fun loadUserByToken(id: UUID): User =
        userJpaRepository.findById(id).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }
}
