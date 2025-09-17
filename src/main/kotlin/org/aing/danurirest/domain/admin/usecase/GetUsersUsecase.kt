package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UserResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUsersUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(): List<UserResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val users = userJpaRepository.findAllByCompanyId(companyId)
        return users.map { UserResponse.from(it) }
    }
}
