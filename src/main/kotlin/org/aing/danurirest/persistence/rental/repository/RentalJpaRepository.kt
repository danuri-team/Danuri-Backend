package org.aing.danurirest.persistence.rental.repository

import org.aing.danurirest.persistence.rental.entity.Rental
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface RentalJpaRepository : JpaRepository<Rental, UUID> {
    fun findAllByUsageId(usageId: UUID): MutableList<Rental>

    @Query("SELECT r FROM Rental r JOIN r.item i WHERE r.id = :rentalId AND i.company.id = :companyId")
    fun findByIdAndCompanyId(
        @Param("rentalId") rentalId: UUID,
        @Param("companyId") companyId: UUID,
    ): Rental?
}
