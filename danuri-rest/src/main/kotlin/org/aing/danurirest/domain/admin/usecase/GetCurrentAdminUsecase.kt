package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class GetCurrentAdminUsecase(
    private val adminRepository: AdminRepository,
) {
    fun execute(): AdminResponse {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto

        val admin =
            adminRepository
                .findByID(user.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        return AdminResponse.from(admin)
    }
} 