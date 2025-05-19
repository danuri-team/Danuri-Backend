package org.aing.danuridomain.persistence.usage.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.aing.danuridomain.persistence.item.entity.QItem
import org.aing.danuridomain.persistence.rental.entity.QRental
import org.aing.danuridomain.persistence.space.entity.QSpace
import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danuridomain.persistence.usage.dto.DetailRentedItemInfo
import org.aing.danuridomain.persistence.usage.dto.DetailUsageInfo
import org.aing.danuridomain.persistence.usage.dto.QCurrentUsageFlatDto
import org.aing.danuridomain.persistence.usage.entity.QUsageHistory
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danuridomain.persistence.user.entity.QUser
import org.aing.danuridomain.persistence.user.entity.User
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Repository
class UsageHistoryRepositoryImpl(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory,
) : UsageHistoryRepository {
    override fun spaceUsingTime(
        spaceId: UUID,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<UsageHistory> =
        usageHistoryJpaRepository.spaceUsingTime(
            spaceId = spaceId,
            startTime = startTime,
            endTime = endTime,
        )

    override fun spaceUsingInfo(
        usageId: UUID,
        userId: UUID,
    ): Optional<UsageHistory> = usageHistoryJpaRepository.findByIdAndUserId(id = usageId, userId = userId)

    override fun createSpaceUsage(
        space: Space,
        user: User,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
    ): UsageHistory =
        usageHistoryJpaRepository.save(
            UsageHistory(
                user = user,
                space = space,
                startAt = startAt,
                endAt = endAt,
            ),
        )

    override fun findById(usageId: UUID): Optional<UsageHistory> = usageHistoryJpaRepository.findById(usageId)

    override fun findAllByCompanyIdAndDateRange(
        companyId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<UsageHistory> = usageHistoryJpaRepository.findAllByCompanyIdAndDateRange(companyId, startDate, endDate)

    override fun findAllBySpaceIdAndDateRange(
        spaceId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<UsageHistory> = usageHistoryJpaRepository.findAllBySpaceIdAndDateRange(spaceId, startDate, endDate)

    override fun findAllByUserIdAndDateRange(
        userId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<UsageHistory> = usageHistoryJpaRepository.findAllByUserIdAndDateRange(userId, startDate, endDate)

    override fun findUserCurrentUsageInfo(userId: UUID): CurrentUsageHistoryDto {
        val usage = QUsageHistory.usageHistory
        val user = QUser.user
        val space = QSpace.space
        val rental = QRental.rental
        val item = QItem.item

        val results =
            queryFactory
                .select(
                    QCurrentUsageFlatDto(
                        user.id,
                        space.id,
                        space.name,
                        usage.startAt,
                        usage.endAt,
                        item.name,
                        rental.quantity,
                        rental.returnedQuantity,
                    ),
                ).from(usage)
                .join(usage.user, user)
                .join(usage.space, space)
                .leftJoin(usage.rental, rental)
                .leftJoin(rental.item, item)
                .where(
                    user.id.eq(userId),
                    usage.endAt.isNull,
                ).fetch()

        if (results.isEmpty()) {
            throw NoSuchElementException("현재 사용 중인 공간이 없습니다.")
        }

        val first = results.first()

        val rentedItems =
            results
                .filter { it.itemName != null }
                .map {
                    DetailRentedItemInfo(
                        itemName = it.itemName!!,
                        quantity = it.quantity ?: 0,
                        returnedQuantity = it.returnedQuantity ?: 0,
                    )
                }

        val usageInfo =
            DetailUsageInfo(
                spaceId = first.spaceId,
                spaceName = first.spaceName,
                startAt = first.startAt,
                endAt = first.endAt,
                rentalItem = rentedItems,
            )

        return CurrentUsageHistoryDto(
            userId = first.userId,
            isUsingSpace = true,
            spaceUsageInfo = usageInfo,
        )
    }
}
