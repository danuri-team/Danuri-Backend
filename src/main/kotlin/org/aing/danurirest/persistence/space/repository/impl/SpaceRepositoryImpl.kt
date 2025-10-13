package org.aing.danurirest.persistence.space.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.aing.danurirest.persistence.device.entity.QDevice
import org.aing.danurirest.persistence.space.dto.BookedTimeRange
import org.aing.danurirest.persistence.space.dto.SpaceWithBookingsDto
import org.aing.danurirest.persistence.space.entity.QSpace
import org.aing.danurirest.persistence.space.repository.SpaceRepository
import org.aing.danurirest.persistence.usage.entity.QUsageHistory
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Repository
class SpaceRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : SpaceRepository {
    override fun findSpacesWithBookingsByDeviceId(
        deviceId: UUID,
        date: LocalDate,
    ): List<SpaceWithBookingsDto> {
        val device = QDevice.device
        val space = QSpace.space
        val usageHistory = QUsageHistory.usageHistory

        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)

        val results =
            queryFactory
                .select(space, usageHistory)
                .from(space)
                .join(device)
                .on(device.company.eq(space.company))
                .leftJoin(usageHistory)
                .on(
                    usageHistory.space.id.eq(space.id),
                    usageHistory.startAt.between(startOfDay, endOfDay),
                ).where(device.id.eq(deviceId))
                .fetch()

        return results
            .groupBy { it.get(space)!! }
            .map { (spaceEntity, tuples) ->
                SpaceWithBookingsDto(
                    space = spaceEntity,
                    bookedRanges =
                        tuples
                            .mapNotNull { it.get(usageHistory) }
                            .map { BookedTimeRange(it.startAt, it.endAt) },
                )
            }
    }
}
