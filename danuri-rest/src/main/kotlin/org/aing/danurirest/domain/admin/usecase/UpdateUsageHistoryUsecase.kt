package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UpdateUsageHistoryRequest
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdateUsageHistoryUsecase(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        usageHistoryId: UUID,
        request: UpdateUsageHistoryRequest,
    ): UsageHistoryResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val usageHistory =
            usageHistoryJpaRepository
                .findById(usageHistoryId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_USAGE_FOUND) }

        if (usageHistory.user.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        usageHistory.endAt = request.endAt

        return UsageHistoryResponse.from(usageHistoryJpaRepository.save(usageHistory))
    }
} 
