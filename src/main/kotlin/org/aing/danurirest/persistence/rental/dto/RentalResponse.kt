package org.aing.danurirest.persistence.rental.dto

import com.querydsl.core.annotations.QueryProjection
import org.aing.danurirest.persistence.rental.RentalStatus
import java.util.*

@QueryProjection
data class RentalResponse(
    val rentalId: UUID,
    val itemId: UUID,
    val itemName: String,
    val userId: UUID,
    val quantity: Int,
    val returnedQuantity: Int,
    val status: RentalStatus,
)
