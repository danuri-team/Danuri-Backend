package org.aing.danurirest.persistence.usage.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.aing.danurirest.global.util.QueryDslUtil
import org.aing.danurirest.persistence.item.entity.QItem
import org.aing.danurirest.persistence.rental.entity.QRental
import org.aing.danurirest.persistence.space.entity.QSpace
import org.aing.danurirest.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danurirest.persistence.usage.dto.DetailRentedItemInfo
import org.aing.danurirest.persistence.usage.dto.DetailUsageInfo
import org.aing.danurirest.persistence.usage.dto.QCurrentUsageFlatDto
import org.aing.danurirest.persistence.usage.entity.QUsageHistory
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.persistence.user.entity.QUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Repository
class UsageHistoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : UsageHistoryRepository {
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
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
    ): List<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qSpace = QSpace.space

        val whereConditions =
            mutableListOf(
                qSpace.company.id.eq(companyId),
            )

        startDate?.let {
            whereConditions.add(qUsage.startAt.goe(it))
            whereConditions.add(qUsage.endAt.isNull.or(qUsage.endAt.goe(it)))
        }

        endDate?.let {
            whereConditions.add(qUsage.startAt.loe(it))
        }

        return queryFactory
            .selectFrom(qUsage)
            .join(qUsage.space, qSpace)
            .where(*whereConditions.toTypedArray())
            .orderBy(qUsage.startAt.desc())
            .fetch()
    }

    override fun findAllByCompanyIdAndDateRange(
        companyId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        pageable: Pageable,
    ): Page<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qSpace = QSpace.space

        val whereConditions =
            mutableListOf(
                qSpace.company.id.eq(companyId),
            )

        startDate?.let {
            whereConditions.add(qUsage.startAt.goe(it))
            whereConditions.add(qUsage.endAt.isNull.or(qUsage.endAt.goe(it)))
        }

        endDate?.let {
            whereConditions.add(qUsage.startAt.loe(it))
        }

        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable, qUsage)

        val query =
            queryFactory
                .selectFrom(qUsage)
                .join(qUsage.space, qSpace)
                .where(*whereConditions.toTypedArray())
                .orderBy(*orderSpecifiers.toTypedArray())

        val results =
            query
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val total =
            queryFactory
                .select(qUsage.count())
                .from(qUsage)
                .join(qUsage.space, qSpace)
                .where(*whereConditions.toTypedArray())
                .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

    override fun findAllByCompanyIdAndSpaceIdAndDateRange(
        spaceId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
    ): List<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qSpace = QSpace.space

        val whereConditions =
            mutableListOf(
                qSpace.id.eq(spaceId),
                qSpace.company.id.eq(companyId),
            )

        startDate?.let {
            whereConditions.add(qUsage.startAt.goe(it))
            whereConditions.add(qUsage.endAt.isNull.or(qUsage.endAt.goe(it)))
        }

        endDate?.let {
            whereConditions.add(qUsage.startAt.loe(it))
        }

        return queryFactory
            .selectFrom(qUsage)
            .join(qUsage.space, qSpace)
            .where(*whereConditions.toTypedArray())
            .orderBy(qUsage.startAt.desc())
            .fetch()
    }

    override fun findAllByCompanyIdAndSpaceIdAndDateRange(
        spaceId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
        pageable: Pageable,
    ): Page<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qSpace = QSpace.space

        val whereConditions =
            mutableListOf(
                qSpace.id.eq(spaceId),
                qSpace.company.id.eq(companyId),
            )

        startDate?.let {
            whereConditions.add(qUsage.startAt.goe(it))
            whereConditions.add(qUsage.endAt.isNull.or(qUsage.endAt.goe(it)))
        }

        endDate?.let {
            whereConditions.add(qUsage.startAt.loe(it))
        }

        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable, qUsage)

        val query =
            queryFactory
                .selectFrom(qUsage)
                .join(qUsage.space, qSpace)
                .where(*whereConditions.toTypedArray())
                .orderBy(*orderSpecifiers.toTypedArray())

        val results = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(qUsage.count())
            .from(qUsage)
            .join(qUsage.space, qSpace)
            .where(*whereConditions.toTypedArray())
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

    override fun findAllByUserIdAndDateRangeAndCompanyId(
        userId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
    ): MutableList<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qUser = QUser.user

        val whereConditions =
            mutableListOf(
                qUser.id.eq(userId),
                qUser.company.id.eq(companyId),
            )

        startDate?.let {
            whereConditions.add(qUsage.startAt.goe(it))
            whereConditions.add(qUsage.endAt.isNull.or(qUsage.endAt.goe(it)))
        }

        endDate?.let {
            whereConditions.add(qUsage.startAt.loe(it))
        }

        return queryFactory
            .selectFrom(qUsage)
            .join(qUsage.user, qUser)
            .where(*whereConditions.toTypedArray())
            .orderBy(qUsage.startAt.desc())
            .fetch()
    }

    override fun findAllByUserIdAndDateRangeAndCompanyId(
        userId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
        pageable: Pageable,
    ): Page<UsageHistory> {
        val qUsage = QUsageHistory.usageHistory
        val qUser = QUser.user

        val whereConditions =
            mutableListOf(
                qUser.id.eq(userId),
                qUser.company.id.eq(companyId),
            )

        startDate?.let {
            whereConditions.add(qUsage.startAt.goe(it))
            whereConditions.add(qUsage.endAt.isNull.or(qUsage.endAt.goe(it)))
        }

        endDate?.let {
            whereConditions.add(qUsage.startAt.loe(it))
        }

        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable, qUsage)

        val query =
            queryFactory
                .selectFrom(qUsage)
                .join(qUsage.user, qUser)
                .where(*whereConditions.toTypedArray())
                .orderBy(*orderSpecifiers.toTypedArray())

        val results = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(qUsage.count())
            .from(qUsage)
            .join(qUsage.user, qUser)
            .where(*whereConditions.toTypedArray())
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

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
                ).orderBy(usage.startAt.desc())
                .fetch()

        if (results.isEmpty()) {
            return CurrentUsageHistoryDto(
                isUsingSpace = false,
            )
        }

        val first = results.first()
        val currentUsageId = first.usageId

        val rentedItems =
            results
                .filter { it.usageId == currentUsageId && it.itemName != null }
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
}