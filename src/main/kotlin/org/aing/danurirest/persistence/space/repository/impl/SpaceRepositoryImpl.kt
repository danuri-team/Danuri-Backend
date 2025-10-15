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

        // 1. Space만 먼저 조회
        val spaces =
            queryFactory
                .selectFrom(space)
                .join(device)
                .on(device.company.eq(space.company))
                .where(device.id.eq(deviceId))
                .fetch()

        if (spaces.isEmpty()) {
            return emptyList()
        }

        // 2. 해당 Space들의 UsageHistory만 별도로 조회
        val spaceIds = spaces.mapNotNull { it.id }
        val usageHistories =
            queryFactory
                .selectFrom(usageHistory)
                .where(
                    usageHistory.space.id.`in`(spaceIds),
                    usageHistory.startAt.between(startOfDay, endOfDay),
                ).fetch()

        // 3. Space ID로 그룹핑
        val usagesBySpaceId = usageHistories.groupBy { it.space.id }

        // 4. DTO 조합
        return spaces.map { spaceEntity ->
            SpaceWithBookingsDto(
                space = spaceEntity,
                bookedRanges =
                    usagesBySpaceId[spaceEntity.id]
                        ?.map { BookedTimeRange(it.startAt, it.endAt) }
                        ?: emptyList(),
            )
        }
    }
}
