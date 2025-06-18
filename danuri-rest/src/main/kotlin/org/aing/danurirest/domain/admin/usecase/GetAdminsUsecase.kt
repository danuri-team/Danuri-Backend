package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.springframework.stereotype.Service

@Service
class GetAdminsUsecase(
    private val adminRepository: AdminRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(): List<AdminResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val admins = adminRepository.findByCompanyId(companyId)
        return admins.map { AdminResponse.from(it) }
    }
} 
