package org.aing.danurirest.domain.item.dto

import java.time.LocalDateTime
import java.util.UUID

data class ItemReturnResponse(
    val id: UUID?,
    val itemId: UUID,
    val itemName: String,
    val totalQuantity: Int,
    val returnedQuantity: Int,
    val returnedAt: LocalDateTime,
) 
