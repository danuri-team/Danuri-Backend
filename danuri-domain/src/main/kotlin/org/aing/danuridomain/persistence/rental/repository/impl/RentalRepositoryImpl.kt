package org.aing.danuridomain.persistence.rental.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.aing.danuridomain.persistence.item.entity.QItem
import org.aing.danuridomain.persistence.rental.dto.QRentalResponse
import org.aing.danuridomain.persistence.rental.dto.RentalResponse
import org.aing.danuridomain.persistence.rental.entity.QRental
import org.aing.danuridomain.persistence.rental.entity.Rental
import org.aing.danuridomain.persistence.rental.repository.RentalJpaRepository
import org.aing.danuridomain.persistence.rental.repository.RentalRepository
import org.aing.danuridomain.persistence.usage.entity.QUsageHistory
import org.aing.danuridomain.persistence.user.entity.QUser
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class RentalRepositoryImpl(
    val rentalJpaRepository: RentalJpaRepository,
    val queryFactory: JPAQueryFactory,
) : RentalRepository {
    override fun save(rental: Rental): Rental = rentalJpaRepository.save(rental)

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
                ),
            ).from(rental)
            .innerJoin(rental.item, item)
            .innerJoin(rental.usage, usage)
            .innerJoin(usage.user, user)
            .where(item.company.id.eq(companyId))
            .fetch()
    }

    override fun delete(rentalId: UUID): Unit = rentalJpaRepository.deleteById(rentalId)

    override fun findById(id: UUID): Optional<Rental> = rentalJpaRepository.findById(id)
}
