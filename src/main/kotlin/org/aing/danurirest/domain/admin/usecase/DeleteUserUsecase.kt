package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteUserUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(userId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userJpaRepository.findByIdAndCompanyId(userId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_USER)

        userJpaRepository.delete(user)
    }
}
