package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.domain.space.dto.UsingSpaceInfoRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FetchSpaceUsingInfoUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(usingSpaceInfoRequest: UsingSpaceInfoRequest): UsageHistory =
        findUsageHistory(usingSpaceInfoRequest.usageId, usingSpaceInfoRequest.userId)

    private fun findUsageHistory(
        usageId: UUID,
        userId: UUID,
    ): UsageHistory =
        usageHistoryRepository
            .spaceUsingInfo(usageId, userId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND) }
}
