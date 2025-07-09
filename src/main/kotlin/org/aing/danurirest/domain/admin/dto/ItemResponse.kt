package org.aing.danurirest.domain.admin.dto

import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.item.entity.Item
import java.util.UUID

data class ItemResponse(
    val id: UUID,
    val name: String,
    val totalQuantity: Int,
    val availableQuantity: Int,
    val status: ItemStatus,
) {
    companion object {
        fun from(entity: Item): ItemResponse =
            ItemResponse(
                id = entity.id!!,
                name = entity.name,
                totalQuantity = entity.totalQuantity,
                availableQuantity = entity.availableQuantity,
                status = entity.status,
            )
    }
}
