package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.domain.admin.dto.AdminPasswordUpdateRequest
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.admin.dto.AdminUpdateRequest
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
    private val passwordEncoder: PasswordEncoder
) {
    fun getAdminInfo(adminId: UUID): AdminResponse {
        val admin = adminRepository.findByID(adminId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }
        
        return AdminResponse.from(admin)
    }
    
    fun getCurrentAdminInfo(): AdminResponse {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        
        val admin = adminRepository.findByID(user.id!!)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }
        
        return AdminResponse.from(admin)
    }
    
    fun getAdminsByCompany(companyId: UUID): List<AdminResponse> {
        // TODO: 조회자 회사에 기반해서만 쿼리가 가능하도록 수정 해야함
        val admins = adminRepository.findByCompanyId(companyId)
        return admins.map { AdminResponse.from(it) }
    }
    
    fun updateAdmin(adminId: UUID, request: AdminUpdateRequest): AdminResponse {
        val admin = adminRepository.findByID(adminId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }
        
        if (admin.email != request.email && adminRepository.existsByEmail(request.email)) {
            throw CustomException(CustomErrorCode.DUPLICATE_EMAIL)
        }

        val updatedAdmin = Admin(
            id = admin.id,
            company = admin.company,
            email = request.email,
            password = admin.password,
            phone = request.phone,
            role = request.role,
            status = admin.status
        )
        
        return AdminResponse.from(adminRepository.update(updatedAdmin))
    }
    
    fun updatePassword(request: AdminPasswordUpdateRequest): AdminResponse {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        
        val admin = adminRepository.findByID(user.id!!)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (!passwordEncoder.matches(request.currentPassword, admin.password)) {
            throw CustomException(CustomErrorCode.INVALID_PASSWORD)
        }
        
        val updatedAdmin = Admin(
            id = admin.id,
            company = admin.company,
            email = admin.email,
            password = passwordEncoder.encode(request.newPassword),
            phone = admin.phone,
            role = admin.role,
            status = admin.status
        )
        
        return AdminResponse.from(adminRepository.update(updatedAdmin))
    }
    
    fun deleteAdmin(adminId: UUID) {
        // TODO: 조회자 회사에 기반해서만 쿼리가 가능하도록 수정 해야함
        adminRepository.findByID(adminId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }
        
        adminRepository.delete(adminId)
    }
} 