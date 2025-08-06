package org.aing.danurirest.persistence.rental.repository

import org.aing.danurirest.persistence.rental.entity.Rental
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RentalJpaRepository : JpaRepository<Rental, UUID> {
    fun findAllByUsageId(usageId: UUID): MutableList<Rental>
}
