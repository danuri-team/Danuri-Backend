package org.aing.danuridomain.persistence.rental.repository.impl

import org.aing.danuridomain.persistence.rental.entity.Rental
import org.aing.danuridomain.persistence.rental.repository.RentalJpaRepository
import org.aing.danuridomain.persistence.rental.repository.RentalRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class RentalRepositoryImpl(
    private val rentalJpaRepository: RentalJpaRepository,
) : RentalRepository {
    override fun save(rental: Rental): Rental = rentalJpaRepository.save(rental)

    override fun findById(id: UUID): Optional<Rental> = rentalJpaRepository.findById(id)
}
