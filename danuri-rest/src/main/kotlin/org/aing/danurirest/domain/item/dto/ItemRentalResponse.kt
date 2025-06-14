package org.aing.danurirest.domain.item.dto

import java.time.LocalDateTime
import java.util.UUID

data class ItemRentalResponse(
    val id: UUID?,
    val itemId: UUID,
    val itemName: String,
    val quantity: Int,
    val borrowedAt: LocalDateTime,
    val returnedQuantity: Int,
) 
