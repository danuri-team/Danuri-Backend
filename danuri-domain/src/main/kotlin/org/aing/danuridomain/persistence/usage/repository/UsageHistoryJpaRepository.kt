package org.aing.danuridomain.persistence.usage.repository

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

interface UsageHistoryJpaRepository : JpaRepository<UsageHistory, UUID> {
    @Query(
        """
            SELECT u FROM UsageHistory u
            WHERE u.space.id = :spaceId
            AND (
                u.start_at < :endTime
                AND (u.end_at IS NULL OR u.end_at > :startTime)
            )
        """,
    )
    fun spaceUsingTime(
        @Param("spaceId") spaceId: UUID,
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime,
    ): List<UsageHistory>

    fun findByIdAndUserId(
        id: UUID,
        userId: UUID,
    ): Optional<UsageHistory>
}
