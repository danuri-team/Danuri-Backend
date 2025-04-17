package org.aing.danuridomain.persistence.usage.repository

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

interface UsageHistoryRepository {
    fun spaceUsingTime(
        spaceId: UUID,
        startTime: LocalDateTime = LocalDateTime.now(),
        endTime: LocalDateTime = LocalDateTime.now().plusMinutes(150),
    ): List<UsageHistory>

    fun spaceUsingInfo(
        usageId: UUID,
        userId: UUID,
    ): Optional<UsageHistory>
}
