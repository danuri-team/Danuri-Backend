package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetAdminCompanyIdUsecase(
    private val adminJpaRepository: AdminJpaRepository,
) {
    fun execute(): UUID {
        val context = SecurityContextHolder.getContext().authentication.principal as ContextDto

        val adminId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        val admin =
            adminJpaRepository
                .findById(adminId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        return admin.company.id ?: throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY)
    }
}
