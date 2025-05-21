package org.aing.danurirest.domain.item.dto

import org.aing.danuridomain.persistence.item.entity.Item
import java.util.UUID

data class ItemListResponse(
    val id: UUID?,
    val name: String,
    val availableQuantity: Int,
) {
    companion object {
        fun from(entity: Item) =
            ItemListResponse(
                id = entity.id,
                name = entity.name,
                availableQuantity = entity.availableQuantity,
            )
    }
}
