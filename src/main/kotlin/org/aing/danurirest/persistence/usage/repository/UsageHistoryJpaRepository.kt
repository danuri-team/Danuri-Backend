package org.aing.danurirest.persistence.usage.repository

import org.aing.danurirest.persistence.usage.entity.UsageHistory
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
                u.startAt < :endTime
                AND (u.endAt IS NULL OR u.endAt > :startTime)
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

    @Query(
        """
        SELECT u FROM UsageHistory u
        JOIN FETCH u.user
        JOIN FETCH u.space
        WHERE u.user.id = :userId
        AND u.startAt <= :currentTime
        AND (u.endAt IS NULL OR u.endAt >= :currentTime)
    """,
    )
    fun findCurrentUsageByUserId(
        @Param("userId") userId: UUID,
        @Param("currentTime") currentTime: LocalDateTime,
    ): List<UsageHistory>

    fun findByUserId(userId: UUID): Optional<UsageHistory>
}
