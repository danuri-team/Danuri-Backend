package org.aing.danurirest.domain.space.dto

import org.aing.danuridomain.persistence.rental.entity.Rental
import java.time.LocalDateTime
import java.util.UUID

data class RentalSimpleResponse(
    val id: UUID?,
    val itemId: UUID?,
    val name: String,
    val borrowedAt: LocalDateTime,
    val returnedAt: LocalDateTime?,
    val isReturned: Boolean,
) {
    companion object {
        fun from(rental: Rental): RentalSimpleResponse =
            RentalSimpleResponse(
                id = rental.id,
                itemId = rental.item.id,
                name = rental.item.name,
                borrowedAt = rental.borrowed_at,
                returnedAt = rental.returned_at,
                isReturned = rental.isReturned,
            )
    }
}
