package org.aing.danurirest.domain.admin.dto

import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.item.enum.ItemStatus
import java.util.UUID

data class ItemResponse(
    val id: UUID,
    val companyId: UUID,
    val companyName: String,
    val name: String,
    val totalQuantity: Int,
    val availableQuantity: Int,
    val status: ItemStatus
) {
    companion object {
        fun from(entity: Item): ItemResponse =
            ItemResponse(
                id = entity.id!!,
                companyId = entity.company.id!!,
                companyName = entity.company.name,
                name = entity.name,
                totalQuantity = entity.total_quantity,
                availableQuantity = entity.available_quantity,
                status = entity.status
            )
    }
} 