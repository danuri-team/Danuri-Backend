package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.domain.auth.user.dto.UserRegisterRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RegisterUserUsecase(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository
) {
    fun execute(request: UserRegisterRequest): User {
        // 전화번호 중복 확인
        if (userRepository.existsByPhone(request.phone)) {
            throw CustomException(CustomErrorCode.DUPLICATE_USER)
        }
        
        // 회사 존재 확인
        val company = companyRepository.findById(request.companyId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        
        // 사용자 생성
        val user = User(
            company = company,
            name = request.name,
            sex = request.sex,
            age = request.age,
            phone = request.phone,
            create_at = LocalDateTime.now(),
            update_at = LocalDateTime.now()
        )
        
        return userRepository.save(user)
    }
} 