package org.aing.danurirest.persistence.usage.repository

import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
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
    fun findUsagesBySpaceAndTimeRange(
        @Param("spaceId") spaceId: UUID,
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime,
    ): List<UsageHistory>

    @Query(
        """
            SELECT u FROM UsageHistory u
            JOIN u.user usr
            WHERE u.id = :id AND usr.company.id = :companyId
        """,
    )
    fun findByIdAndUserCompanyId(
        @Param("id") id: UUID,
        @Param("companyId") companyId: UUID,
    ): UsageHistory?

    fun findByEndAtBetweenAndNotifiedAtIsNull(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<UsageHistory>
}
