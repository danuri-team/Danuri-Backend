package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DeleteAdminUsecase(
    private val adminRepository: AdminRepository,
) {
    fun execute() {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        val adminId = user.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        adminRepository.delete(adminId)
    }
} 