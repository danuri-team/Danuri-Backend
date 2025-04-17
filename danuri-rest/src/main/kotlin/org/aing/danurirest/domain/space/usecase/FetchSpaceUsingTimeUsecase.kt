package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.impl.UsageHistoryRepositoryImpl
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FetchSpaceUsingTimeUsecase(
    private val usageHistoryRepositoryImpl: UsageHistoryRepositoryImpl,
) {
    fun execute(spaceId: UUID): List<UsageHistory> =
        usageHistoryRepositoryImpl.spaceUsingTime(
            spaceId = spaceId,
        )
}
