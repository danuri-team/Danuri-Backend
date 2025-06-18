package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.admin.dto.AdminUpdateRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service

@Service
class UpdateAdminUsecase(
    private val adminRepository: AdminRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: AdminUpdateRequest): AdminResponse {
        val currentAdminCompanyId = getAdminCompanyIdUsecase.execute()
        val admin =
            adminRepository
                .findByID(request.id)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (admin.company.id != currentAdminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        if (admin.email != request.email && adminRepository.existsByEmail(request.email)) {
            throw CustomException(CustomErrorCode.DUPLICATE_EMAIL)
        }

        val updatedAdmin =
            Admin(
                id = admin.id,
                company = admin.company,
                email = request.email,
                password = admin.password,
                phone = request.phone,
                role = request.role,
                status = admin.status,
            )

        return AdminResponse.from(adminRepository.save(updatedAdmin))
    }
} 