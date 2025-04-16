package org.aing.danuridomain.persistence.space.repository

import org.aing.danuridomain.persistence.space.entity.Space
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

interface SpaceJpaRepository : JpaRepository<Space, UUID> {
    @Query(
        """
            SELECT s FROM Space s
            JOIN s.company c
            LEFT JOIN s.usage u
            WHERE c.id = :companyId
            AND :targetTime >= s.startAt
            AND :targetTime < s.endAt
            AND (
                u IS NULL OR
                :targetDateTime < u.startAt OR
                :targetDateTime >= u.endAt
            )
        """,
    )
    fun findAvailableSpaces(
        @Param("companyId") companyId: UUID,
        @Param("targetTime") targetTime: LocalTime,
        @Param("targetDateTime") targetDateTime: LocalDateTime,
    ): List<Space>

    @Query(
        """
            SELECT u FROM UsageHistory u
            WHERE u.space.id = :spaceId
            AND (
            u.startAt < :endTime AND (u.endAt IS NULL OR u.endAt > :startTime)
            )
        """,
    )
    fun spaceUsingTime(
        @Param("spaceId") spaceId: UUID,
        @Param("startTime") startTime: LocalDateTime = LocalDateTime.now(),
        @Param("endTime") endTime: LocalDateTime = startTime.plusMinutes(150),
    ): List<Space>
}
