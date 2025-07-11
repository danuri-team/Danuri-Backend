package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UserRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdateUserUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        userId: UUID,
        request: UserRequest,
    ) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userJpaRepository
                .findById(userId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        if (user.phone != request.phone) {
            userJpaRepository
                .findByPhoneAndCompanyId(request.phone, user.company.id!!)
                .ifPresent { throw CustomException(CustomErrorCode.DUPLICATE_USER) }
        }

        user.name = request.name
        user.sex = request.sex
        user.age = request.age
        user.phone = request.phone

        userJpaRepository.save(user)
    }
}
