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
            WHERE u.space.company.id = :companyId
        """,
    )
    fun findAllByCompanyId(
        @Param("companyId") companyId: UUID,
    ): List<UsageHistory>

    @Query(
        """
        SELECT u FROM UsageHistory u
        JOIN FETCH u.user
        JOIN FETCH u.space
        WHERE u.space.company.id = :companyId
        AND u.startAt <= :endDate
        AND (u.endAt IS NULL OR u.endAt >= :startDate)
    """,
    )
    fun findAllByCompanyIdAndDateRange(
        @Param("companyId") companyId: UUID,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
    ): List<UsageHistory>

    @Query(
        """
            SELECT u FROM UsageHistory u
            JOIN FETCH u.user
            JOIN FETCH u.space
            WHERE u.user.id = :userId
            AND u.startAt >= :startDate
            AND (u.endAt IS NULL OR u.endAt <= :endDate)
        """,
    )
    fun findAllByUserIdAndDateRange(
        @Param("userId") userId: UUID,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
    ): List<UsageHistory>
}
