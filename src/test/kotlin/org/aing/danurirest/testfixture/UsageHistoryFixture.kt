package org.aing.danurirest.testfixture

import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.user.entity.User
import java.time.LocalDateTime
import java.util.*

object UsageHistoryFixture {
    fun createTestUsageHistory(
        user: User = UserFixture.createTestUser(),
        space: Space = SpaceFixture.createTestSpace(),
        startAt: LocalDateTime = LocalDateTime.now(),
        endAt: LocalDateTime = LocalDateTime.now().plusMinutes(30),
    ): UsageHistory {
        return UsageHistory(
            id = UUID.randomUUID(),
            user = user,
            space = space,
            startAt = startAt,
            endAt = endAt,
        )
    }
}