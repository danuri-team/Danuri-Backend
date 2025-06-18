package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetAdminUsecase(
    private val adminRepository: AdminRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(adminId: UUID): AdminResponse {
        val currentAdminCompanyId = getAdminCompanyIdUsecase.execute()
        val admin =
            adminRepository
                .findById(adminId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (admin.company.id != currentAdminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        return AdminResponse.from(admin)
    }
} 
