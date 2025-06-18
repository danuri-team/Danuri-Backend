package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteUserUsecase(
    private val userRepository: UserRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(userId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userRepository
                .findById(userId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        userRepository.delete(user)
    }
} 
