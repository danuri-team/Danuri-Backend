package org.aing.danurirest.domain.usage.dto

import java.util.UUID

data class RentedItemList(
    val id: UUID?,
    val name: String,
    val quantity: Int,
    val returnedQuantity: Int,
)
