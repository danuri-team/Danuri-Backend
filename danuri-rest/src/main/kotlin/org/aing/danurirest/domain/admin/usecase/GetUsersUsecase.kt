package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.domain.admin.dto.UserResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.springframework.stereotype.Service

@Service
class GetUsersUsecase(
    private val userRepository: UserRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(): List<UserResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val users = userRepository.findByCompanyId(companyId)
        return users.map { UserResponse.from(it) }
    }
} 
