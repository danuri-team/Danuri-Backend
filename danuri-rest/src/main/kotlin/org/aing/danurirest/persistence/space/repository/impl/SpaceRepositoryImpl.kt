package org.aing.danurirest.persistence.space.repository.impl

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.aing.danurirest.persistence.device.entity.QDevice
import org.aing.danurirest.persistence.space.dto.SpaceAvailabilityDto
import org.aing.danurirest.persistence.space.entity.QSpace
import org.aing.danurirest.persistence.space.repository.SpaceRepository
import org.aing.danurirest.persistence.usage.entity.QUsageHistory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Repository
class SpaceRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : SpaceRepository {
    override fun findSpacesWithAvailabilityByDeviceId(deviceId: UUID): List<SpaceAvailabilityDto> {
        val currentDateTime = LocalDateTime.now()
        val currentTime = currentDateTime.toLocalTime()

        val device = QDevice.device
        val space = QSpace.space
        val usageHistory = QUsageHistory.usageHistory

        return queryFactory
            .select(
                Projections.constructor(
                    SpaceAvailabilityDto::class.java,
                    space,
                    isWithinOperatingHours(space, currentTime)
                        .and(
                            JPAExpressions
                                .selectOne()
                                .from(usageHistory)
                                .where(
                                    usageHistory.space.id.eq(space.id),
                                    isCurrentlyInUse(usageHistory, currentDateTime),
                                ).notExists(),
                        ),
                ),
            ).from(space)
            .join(device)
            .on(device.company.eq(space.company))
            .where(device.id.eq(deviceId))
            .fetch()
    }

    private fun isCurrentlyInUse(
        usageHistory: QUsageHistory,
        currentDateTime: LocalDateTime,
    ): BooleanExpression =
        usageHistory.startAt
            .loe(currentDateTime)
            .and(
                usageHistory.endAt
                    .isNull()
                    .or(usageHistory.endAt.goe(currentDateTime)),
            )

    private fun isWithinOperatingHours(
        space: QSpace,
        currentTime: LocalTime,
    ): BooleanExpression = space.startAt.loe(currentTime).and(space.endAt.goe(currentTime))
}
