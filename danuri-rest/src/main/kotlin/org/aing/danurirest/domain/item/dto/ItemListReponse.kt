package org.aing.danurirest.domain.item.dto

import org.aing.danuridomain.persistence.item.entity.Item
import java.util.UUID

data class ItemListReponse(
    val id: UUID?,
    val name: String,
    val availableQuantity: Int,
) {
    companion object {
        fun from(entity: Item) =
            ItemListReponse(
                id = entity.id,
                name = entity.name,
                availableQuantity = entity.availableQuantity,
            )
    }
}
