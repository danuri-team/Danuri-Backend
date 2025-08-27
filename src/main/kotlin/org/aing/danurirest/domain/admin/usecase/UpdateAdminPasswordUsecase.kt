package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.AdminPasswordUpdateRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UpdateAdminPasswordUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun execute(request: AdminPasswordUpdateRequest) {
        val user = PrincipalUtil.getContextDto()

        val admin =
            adminJpaRepository
                .findById(user.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        admin.password = passwordEncoder.encode(request.newPassword)

        adminJpaRepository.save(admin)
    }
}
