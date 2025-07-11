package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DeleteAdminUsecase(
    private val adminJpaRepository: AdminJpaRepository,
) {
    fun execute() {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        val admin = adminJpaRepository.findById(user.id!!).orElseThrow { CustomException(CustomErrorCode.VALIDATION_ERROR) }
        adminJpaRepository.delete(admin)
    }
} 
