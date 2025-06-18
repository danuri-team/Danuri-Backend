package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminPasswordUpdateRequest
import org.aing.danurirest.domain.admin.dto.AdminResponse
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
    fun execute(request: AdminPasswordUpdateRequest): AdminResponse {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto

        val admin =
            adminRepository
                .findByID(user.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (!passwordEncoder.matches(request.currentPassword, admin.password)) {
            throw CustomException(CustomErrorCode.INVALID_PASSWORD)
        }

        val updatedAdmin =
            Admin(
                id = admin.id,
                company = admin.company,
                email = admin.email,
                password = passwordEncoder.encode(request.newPassword),
                phone = admin.phone,
                role = admin.role,
                status = admin.status,
            )

        return AdminResponse.from(adminRepository.save(updatedAdmin))
    }
} 