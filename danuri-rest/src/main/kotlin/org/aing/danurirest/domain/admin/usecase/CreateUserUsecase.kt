package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.domain.admin.dto.UserRequest
import org.aing.danurirest.domain.admin.dto.UserResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateUserUsecase(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: UserRequest): UserResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        if (request.companyId != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val company =
            companyRepository
                .findById(request.companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        userRepository
            .findByPhoneAndCompanyId(request.phone, request.companyId)
            .ifPresent { throw CustomException(CustomErrorCode.DUPLICATE_USER) }

        val user =
            User(
                company = company,
                name = request.name,
                sex = request.sex,
                age = request.age,
                phone = request.phone,
            )

        return UserResponse.from(userRepository.save(user))
    }
} 
