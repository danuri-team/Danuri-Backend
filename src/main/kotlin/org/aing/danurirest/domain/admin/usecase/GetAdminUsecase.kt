package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetAdminUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(adminId: UUID): AdminResponse {
        val currentAdminCompanyId = getAdminCompanyIdUsecase.execute()
        val admin =
            adminJpaRepository.findByIdAndCompanyId(adminId, currentAdminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN)

        return AdminResponse.from(admin)
    }
}
