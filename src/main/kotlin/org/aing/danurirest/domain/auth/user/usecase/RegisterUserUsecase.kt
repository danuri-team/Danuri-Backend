package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.user.dto.UserRegisterRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.user.entity.User
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service

@Service
class RegisterUserUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val companyJpaRepository: CompanyJpaRepository,
) {
    fun execute(request: UserRegisterRequest) {
        if (userJpaRepository.existsByPhone(request.phone)) {
            throw CustomException(CustomErrorCode.DUPLICATE_USER)
        }

        val company =
            companyJpaRepository
                .findById(request.companyId)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val user =
            User(
                company = company,
                phone = request.phone,
            )

        userJpaRepository.save(user)
    }
}
