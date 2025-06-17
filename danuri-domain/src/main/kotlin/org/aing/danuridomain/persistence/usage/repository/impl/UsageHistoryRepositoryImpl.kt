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

    override fun findByIdAndCompanyId(
        usageId: UUID,
        companyId: UUID,
    ): Optional<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qSpace = QSpace.space

        val result =
            queryFactory
                .selectFrom(qUsage)
                .join(qUsage.space, qSpace)
                .where(
                    qUsage.id.eq(usageId),
                    qSpace.company.id.eq(companyId),
                ).fetchOne()

        return Optional.ofNullable(result)
    }

    override fun findAllByCompanyIdAndDateRange(
        companyId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<UsageHistory> = usageHistoryJpaRepository.findAllByCompanyIdAndDateRange(companyId, startDate, endDate)

    override fun findAllByCompanyIdAndSpaceIdAndDateRange(
        spaceId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        companyId: UUID,
    ): List<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qSpace = QSpace.space

        return queryFactory
            .selectFrom(qUsage)
            .join(qUsage.space, qSpace)
            .fetchJoin()
            .where(
                qSpace.id.eq(spaceId),
                qSpace.company.id.eq(companyId),
                qUsage.startAt.goe(startDate),
                qUsage.startAt.loe(endDate),
            ).fetch()
    }

    override fun findAllByUserIdAndDateRangeAndCompanyId(
        userId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        companyId: UUID,
    ): MutableList<UsageHistory>? {
        val qUsage = QUsageHistory.usageHistory
        val qUser = QUser.user

        return queryFactory
            .selectFrom(qUsage)
            .join(qUsage.user, qUser)
            .fetchJoin()
            .where(
                qUser.id.eq(userId),
                qUser.company.id.eq(companyId),
                qUsage.startAt.between(startDate, endDate),
            ).fetch()
    }

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
                        usage.id,
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
                    usage.endAt.isNull.or(usage.endAt.after(LocalDateTime.now())),
                ).fetch()

        // TODO: Repository Layer -> Service Layer
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
                usageId = first.usageId,
            )

        return CurrentUsageHistoryDto(
            isUsingSpace = true,
            spaceUsageInfo = usageInfo,
        )
    }

    override fun updateEndDate(
        userId: UUID,
        endDate: LocalDateTime,
    ) {
        val result =
            usageHistoryJpaRepository.findByUserId(userId)
        result.endAt = endDate
        usageHistoryJpaRepository.save(result)
    }
}
