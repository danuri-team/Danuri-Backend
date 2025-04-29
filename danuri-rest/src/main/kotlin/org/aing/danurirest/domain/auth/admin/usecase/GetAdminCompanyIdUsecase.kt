package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetAdminCompanyIdUsecase(
    private val adminRepository: AdminRepository
) {
    fun execute(): UUID {
        // SecurityContextHolder에서 현재 인증된 관리자의 정보를 가져옴
        val context = SecurityContextHolder.getContext().authentication.principal as ContextDto
        
        // 관리자 ID 확인
        val adminId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
        
        // 관리자 정보 조회
        val admin = adminRepository.findByID(adminId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }
        
        // 관리자의 회사 ID 반환
        return admin.company.id ?: throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY)
    }
} 