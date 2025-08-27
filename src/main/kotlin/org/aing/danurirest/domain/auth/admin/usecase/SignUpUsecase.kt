package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danurirest.domain.auth.admin.dto.SignUpAdminRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class SignUpUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val companyJpaRepository: CompanyJpaRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun execute(signUpAdminRequest: SignUpAdminRequest) {
        if (adminJpaRepository.existsByEmail(signUpAdminRequest.email)) {
            throw CustomException(CustomErrorCode.DUPLICATE_EMAIL)
        }

        val company: Company =
            companyJpaRepository.findById(signUpAdminRequest.companyId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY)
            }

        adminJpaRepository.save(
            Admin(
                company = company,
                email = signUpAdminRequest.email,
                password = passwordEncoder.encode(signUpAdminRequest.password),
                phone = signUpAdminRequest.phone,
                helpSetting = company.helpSetting,
            ),
        )
    }
}
