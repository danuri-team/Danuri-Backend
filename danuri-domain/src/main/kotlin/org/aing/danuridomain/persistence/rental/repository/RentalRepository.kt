package org.aing.danuridomain.persistence.rental.repository

import org.aing.danuridomain.persistence.rental.entity.Rental
import java.util.Optional
import java.util.UUID

interface RentalRepository {
    fun save(rental: Rental): Rental

    fun findById(id: UUID): Optional<Rental>
}
