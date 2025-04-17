package org.aing.danuridomain.persistence.usage.repository.impl

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Repository
class UsageHistoryRepositoryImpl(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
) : UsageHistoryRepository {
    override fun spaceUsingTime(
        spaceId: UUID,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<UsageHistory> =
        usageHistoryJpaRepository.spaceUsingTime(
            spaceId = spaceId,
            startTime = startTime,
            endTime = endTime,
        )

    override fun spaceUsingInfo(
        usageId: UUID,
        userId: UUID,
    ): Optional<UsageHistory> = usageHistoryJpaRepository.findByIdAndUserId(id = usageId, userId = userId)
}
