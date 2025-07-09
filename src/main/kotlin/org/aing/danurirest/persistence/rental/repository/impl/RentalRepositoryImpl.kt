package org.aing.danurirest.persistence.rental.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.aing.danurirest.persistence.item.entity.QItem
import org.aing.danurirest.persistence.rental.dto.QRentalResponse
import org.aing.danurirest.persistence.rental.dto.RentalResponse
import org.aing.danurirest.persistence.rental.entity.QRental
import org.aing.danurirest.persistence.rental.repository.RentalRepository
import org.aing.danurirest.persistence.usage.entity.QUsageHistory
import org.aing.danurirest.persistence.user.entity.QUser
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
                        user.name,
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

    override fun findByCompanyId(companyId: UUID): MutableList<RentalResponse> {
        val rental = QRental.rental
        val item = QItem.item
        val usage = QUsageHistory.usageHistory
        val user = QUser.user

        return queryFactory
            .select(
                QRentalResponse(
                    rental.id,
                    item.id,
                    item.name,
                    user.name,
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
            .fetch()
    }
}
