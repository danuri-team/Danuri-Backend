package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.auth.admin.dto.AdminInfoResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class FetchAdminInfoUsecase(
    private val adminRepository: AdminRepository,
) {
    fun execute(): AdminInfoResponse {
        val user: ContextDto =
            SecurityContextHolder
                .getContext()
                .authentication.principal
                as ContextDto

        return AdminInfoResponse.from(
            adminRepository
                .findByID(user.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) },
        )
    }
}
