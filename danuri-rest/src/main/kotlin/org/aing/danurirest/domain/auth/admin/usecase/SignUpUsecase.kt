package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danuridomain.persistence.admin.Status
import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.user.Role
import org.aing.danurirest.domain.auth.admin.dto.SignUpAdminRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class SignUpUsecase(
    private val adminRepository: AdminRepository,
    private val companyRepository: CompanyRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun execute(signUpAdminRequest: SignUpAdminRequest) {
        if (adminRepository.existsByEmail(signUpAdminRequest.email)) {
            throw CustomException(CustomErrorCode.DUPLICATE_EMAIL)
        }

        val company: Company =
            companyRepository.findById(signUpAdminRequest.companyId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY)
            }

        adminRepository.save(
            Admin(
                company = company,
                email = signUpAdminRequest.email,
                password = passwordEncoder.encode(signUpAdminRequest.password),
                phone = signUpAdminRequest.phone,
                role = Role.ROLE_ADMIN,
                status = Status.NEED_COMPANY_APPROVE,
            ),
        )
    }
}
