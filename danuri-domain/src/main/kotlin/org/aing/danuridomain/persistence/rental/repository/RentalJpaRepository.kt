package org.aing.danuridomain.persistence.rental.repository

import org.aing.danuridomain.persistence.rental.entity.Rental
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RentalJpaRepository: JpaRepository<Rental, UUID>