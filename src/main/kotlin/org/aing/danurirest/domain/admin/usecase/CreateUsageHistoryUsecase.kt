package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UsageHistoryCreateRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateUsageHistoryUsecase(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val userRepository: UserJpaRepository,
    private val spaceJpaRepository: SpaceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional
    fun execute(request: UsageHistoryCreateRequest) {
        if (request.startAt.isAfter(request.endAt)) {
            throw CustomException(CustomErrorCode.VALIDATION_ERROR)
        }

        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userRepository.findByIdAndCompanyId(request.userId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_USER)

        val space =
            spaceJpaRepository.findByIdAndCompanyId(request.spaceId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_SPACE)

        val usageHistory =
            UsageHistory(
                user = user,
                space = space,
                startAt = request.startAt,
                endAt = request.endAt,
            )

        usageHistoryJpaRepository.save(usageHistory)
    }
}
