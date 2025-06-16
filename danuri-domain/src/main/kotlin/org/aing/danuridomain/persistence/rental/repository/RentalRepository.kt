package org.aing.danuridomain.persistence.rental.repository

import org.aing.danuridomain.persistence.rental.dto.RentalResponse
import org.aing.danuridomain.persistence.rental.entity.Rental
import java.util.Optional
import java.util.UUID

interface RentalRepository {
    fun save(rental: Rental): Rental

    fun findByIdAndCompanyId(
        id: UUID,
        companyId: UUID,
    ): Optional<RentalResponse>

    fun findByCompanyId(companyId: UUID): MutableList<RentalResponse>

    fun delete(rentalId: UUID)

    fun findById(id: UUID): Optional<Rental>
}
