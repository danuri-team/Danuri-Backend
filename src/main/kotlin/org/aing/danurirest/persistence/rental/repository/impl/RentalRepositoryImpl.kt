package org.aing.danurirest.persistence.rental.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.aing.danurirest.global.util.QueryDslUtil
import org.aing.danurirest.persistence.item.entity.QItem
import org.aing.danurirest.persistence.rental.dto.QRentalResponse
import org.aing.danurirest.persistence.rental.dto.RentalResponse
import org.aing.danurirest.persistence.rental.entity.QRental
import org.aing.danurirest.persistence.rental.repository.RentalRepository
import org.aing.danurirest.persistence.usage.entity.QUsageHistory
import org.aing.danurirest.persistence.user.entity.QUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class RentalRepositoryImpl(
    val queryFactory: JPAQueryFactory,
) : RentalRepository {
    override fun findByIdAndCompanyId(
        id: UUID,
        companyId: UUID,
    ): Optional<RentalResponse> {
        val rental = QRental.rental
        val item = QItem.item
        val usage = QUsageHistory.usageHistory
        val user = QUser.user

        val result =
            queryFactory
                .select(
                    QRentalResponse(
                        rental.id,
                        item.id,
                        item.name,
                        user.id,
                        rental.quantity,
                        rental.returnedQuantity,
                        rental.status,
                    ),
                ).from(rental)
                .innerJoin(rental.item, item)
                .innerJoin(rental.usage, usage)
                .innerJoin(usage.user, user)
                .where(
                    rental.id
                        .eq(id)
                        .and(
                            item.company.id.eq(companyId),
                        ),
                ).fetchOne()

        return Optional.ofNullable(result)
    }

    override fun findByCompanyId(
        companyId: UUID,
        pageable: Pageable,
    ): Page<RentalResponse> {
        val rental = QRental.rental
        val item = QItem.item
        val usage = QUsageHistory.usageHistory
        val user = QUser.user

        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable, rental)

        val query =
            queryFactory
                .select(
                    QRentalResponse(
                        rental.id,
                        item.id,
                        item.name,
                        user.id,
                        rental.quantity,
                        rental.returnedQuantity,
                        rental.status,
                    ),
                ).from(rental)
                .innerJoin(rental.item, item)
                .innerJoin(rental.usage, usage)
                .innerJoin(usage.user, user)
                .where(item.company.id.eq(companyId))
                .orderBy(*orderSpecifiers.toTypedArray())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())

        val results = query.fetch()

        val total = queryFactory.select(rental.count())
            .from(rental)
            .innerJoin(rental.item, item)
            .where(item.company.id.eq(companyId))
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }
}