package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.springframework.stereotype.Service

@Service
class GetAdminsUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(): List<AdminResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val admins = adminJpaRepository.findAllByCompanyId(companyId)
        return admins.map { AdminResponse.from(it) }
    }
}
