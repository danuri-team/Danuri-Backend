package org.aing.danuridomain.persistence.usage.repository

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.user.entity.User
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

    fun createSpaceUsage(
        space: Space,
        user: User,
        startAt: LocalDateTime = LocalDateTime.now(),
        endAt: LocalDateTime = LocalDateTime.now().plusMinutes(30),
    ): UsageHistory
}
