package org.aing.danurirest.domain.usage.usecase

import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.domain.usage.dto.FetchCheckOutRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FetchCheckOutUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(fetchCheckOutRequest: FetchCheckOutRequest) =
        usageHistoryRepository.updateEndDate(
            usageId = fetchCheckOutRequest.usageId,
            endDate = fetchCheckOutRequest.endAt ?: LocalDateTime.now(),
        )
}
