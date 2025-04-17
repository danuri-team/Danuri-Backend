package org.aing.danurirest.global.security.serivce

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthDetailService(
    private val adminRepository: AdminRepository,
) {
    fun loadUserByUsername(userId: UUID): Admin =
        adminRepository.findById(userId).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }
}
