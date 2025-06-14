package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminPasswordUpdateRequest
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.admin.dto.AdminUpdateRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AdminManagementUsecase(
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun getAdminInfo(adminId: UUID): AdminResponse {
        val currentAdminCompanyId = getAdminCompanyIdUsecase.execute()
        val admin =
            adminRepository
                .findByID(adminId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (admin.company.id != currentAdminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        return AdminResponse.from(admin)
    }

    fun getCurrentAdminInfo(): AdminResponse {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto

        val admin =
            adminRepository
                .findByID(user.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        return AdminResponse.from(admin)
    }

    fun getAdminsByCompany(): List<AdminResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val admins = adminRepository.findByCompanyId(companyId)
        return admins.map { AdminResponse.from(it) }
    }

    fun updateAdmin(request: AdminUpdateRequest): AdminResponse {
        val currentAdminCompanyId = getAdminCompanyIdUsecase.execute()
        val admin =
            adminRepository
                .findByID(request.id ?: currentAdminCompanyId)
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

    fun updatePassword(request: AdminPasswordUpdateRequest): AdminResponse {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto

        val admin =
            adminRepository
                .findByID(user.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (!passwordEncoder.matches(request.currentPassword, admin.password)) {
            throw CustomException(CustomErrorCode.INVALID_PASSWORD)
        }

        val updatedAdmin =
            Admin(
                id = admin.id,
                company = admin.company,
                email = admin.email,
                password = passwordEncoder.encode(request.newPassword),
                phone = admin.phone,
                role = admin.role,
                status = admin.status,
            )

        return AdminResponse.from(adminRepository.save(updatedAdmin))
    }

    fun deleteAdmin() {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        val adminId = user.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        adminRepository.delete(adminId)
    }
}
