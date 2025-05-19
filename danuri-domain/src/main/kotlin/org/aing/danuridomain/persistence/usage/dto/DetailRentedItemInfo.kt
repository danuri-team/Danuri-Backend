package org.aing.danuridomain.persistence.usage.dto

data class DetailRentedItemInfo(
    val itemName: String,
    val quantity: Int,
    val returnedQuantity: Int,
)
