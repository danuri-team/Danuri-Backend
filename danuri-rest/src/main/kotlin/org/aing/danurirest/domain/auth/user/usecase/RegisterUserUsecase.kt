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
        if (userRepository.existsByPhone(request.phone)) {
            throw CustomException(CustomErrorCode.DUPLICATE_USER)
        }
        
        val company = companyRepository.findById(request.companyId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        
        val user = User(
            company = company,
            name = request.name,
            sex = request.sex,
            age = request.age,
            phone = request.phone
        )
        
        return userRepository.save(user)
    }
} 