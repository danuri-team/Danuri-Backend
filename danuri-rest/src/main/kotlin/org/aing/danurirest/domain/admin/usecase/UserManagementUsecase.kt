package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.domain.admin.dto.UserRequest
import org.aing.danurirest.domain.admin.dto.UserResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserManagementUsecase(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository
) {
    fun createUser(request: UserRequest): UserResponse {
        val company = companyRepository.findById(request.companyId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        
        // 중복 전화번호 확인
        userRepository.findByPhoneAndCompanyId(request.phone, request.companyId)
            .ifPresent { throw CustomException(CustomErrorCode.DUPLICATE_USER) }
        
        val now = LocalDateTime.now()
        val user = User(
            company = company,
            name = request.name,
            sex = request.sex,
            age = request.age,
            phone = request.phone,
            create_at = now,
            update_at = now
        )
        
        return UserResponse.from(userRepository.save(user))
    }
    
    fun updateUser(userId: UUID, request: UserRequest): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        
        // 회사가 변경되었는지 확인
        val company = if (user.company.id != request.companyId) {
            companyRepository.findById(request.companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        } else {
            user.company
        }
        
        // 다른 사용자와 전화번호 중복 확인 (수정 시 본인 제외)
        if (user.phone != request.phone) {
            userRepository.findByPhoneAndCompanyId(request.phone, request.companyId)
                .ifPresent { throw CustomException(CustomErrorCode.DUPLICATE_USER) }
        }
        
        val updatedUser = User(
            id = user.id,
            company = company,
            usages = user.usages,
            name = request.name,
            sex = request.sex,
            age = request.age,
            phone = request.phone,
            create_at = user.create_at,
            update_at = LocalDateTime.now()
        )
        
        return UserResponse.from(userRepository.update(updatedUser))
    }
    
    fun deleteUser(userId: UUID) {
        val user = userRepository.findById(userId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        
        // 이용 내역이 있는지 확인
        if (user.usages.isNotEmpty()) {
            throw CustomException(CustomErrorCode.USER_HAS_USAGES)
        }
        
        userRepository.delete(userId)
    }
    
    fun getUser(userId: UUID): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        
        return UserResponse.from(user)
    }
    
    fun getUsersByCompany(companyId: UUID): List<UserResponse> {
        val users = userRepository.findByCompanyId(companyId)
        return users.map { UserResponse.from(it) }
    }
} 