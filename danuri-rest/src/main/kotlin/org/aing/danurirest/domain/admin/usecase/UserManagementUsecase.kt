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
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserManagementUsecase(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun createUser(request: UserRequest): UserResponse {
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

        val now = LocalDateTime.now()
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

    fun updateUser(
        userId: UUID,
        request: UserRequest,
    ): UserResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userRepository
                .findById(userId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        if (user.phone != request.phone) {
            userRepository
                .findByPhoneAndCompanyId(request.phone, user.company.id!!)
                .ifPresent { throw CustomException(CustomErrorCode.DUPLICATE_USER) }
        }

        val updatedUser =
            User(
                id = user.id,
                company = user.company,
                usages = user.usages,
                name = request.name,
                sex = request.sex,
                age = request.age,
                phone = request.phone,
            )

        return UserResponse.from(userRepository.save(updatedUser))
    }

    fun deleteUser(userId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userRepository
                .findById(userId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        userRepository.delete(userId)
    }

    fun getUser(userId: UUID): UserResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userRepository
                .findById(userId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        return UserResponse.from(user)
    }

    fun getCurrentAdminCompanyUsers(): List<UserResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val users = userRepository.findByCompanyId(companyId)
        return users.map { UserResponse.from(it) }
    }
}
