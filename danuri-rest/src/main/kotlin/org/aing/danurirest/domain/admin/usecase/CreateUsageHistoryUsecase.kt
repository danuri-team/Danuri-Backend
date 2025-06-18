package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.domain.admin.dto.CreateUsageHistoryRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateUsageHistoryUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val userRepository: UserRepository,
    private val spaceRepository: SpaceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: CreateUsageHistoryRequest) {
        if (request.startAt.isAfter(request.endAt)) {
            throw CustomException(CustomErrorCode.VALIDATION_ERROR)
        }

        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val user =
            userRepository
                .findById(request.userId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val space =
            spaceRepository
                .findById(request.spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val usageHistory =
            UsageHistory(
                user = user,
                space = space,
                startAt = request.startAt,
                endAt = request.endAt,
            )

        usageHistoryRepository.save(usageHistory)
    }
} 
