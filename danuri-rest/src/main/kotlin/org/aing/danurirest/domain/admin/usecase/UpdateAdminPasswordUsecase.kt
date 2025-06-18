package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminPasswordUpdateRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UpdateAdminPasswordUsecase(
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun execute(request: AdminPasswordUpdateRequest) {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto

        val admin =
            adminRepository
                .findById(user.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (!passwordEncoder.matches(request.currentPassword, admin.password)) {
            throw CustomException(CustomErrorCode.INVALID_PASSWORD)
        }

        admin.password = passwordEncoder.encode(request.newPassword)

        adminRepository.save(admin)
    }
}
