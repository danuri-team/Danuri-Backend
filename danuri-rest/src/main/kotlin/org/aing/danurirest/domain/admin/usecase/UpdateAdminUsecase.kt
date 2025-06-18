package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminUpdateRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateAdminUsecase(
    private val adminRepository: AdminRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: AdminUpdateRequest) {
        val currentAdminCompanyId = getAdminCompanyIdUsecase.execute()
        val admin =
            adminRepository
                .findById(request.id)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (admin.company.id != currentAdminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        if (admin.email != request.email && adminRepository.existsByEmail(request.email)) {
            throw CustomException(CustomErrorCode.DUPLICATE_EMAIL)
        }

        admin.email = request.email
        admin.phone = request.phone
        admin.role = request.role

        adminRepository.save(admin)
    }
}
