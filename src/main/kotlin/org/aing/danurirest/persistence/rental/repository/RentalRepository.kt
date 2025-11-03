package org.aing.danurirest.persistence.rental.repository

import org.aing.danurirest.persistence.rental.dto.RentalResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface RentalRepository {
    fun findByIdAndCompanyId(
        id: UUID,
        companyId: UUID,
    ): Optional<RentalResponse>

    fun findByCompanyId(
        companyId: UUID,
        pageable: Pageable,
    ): Page<RentalResponse>
}
