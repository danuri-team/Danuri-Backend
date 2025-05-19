package org.aing.danurirest.domain.usage.dto

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

data class CurrentUserUsageInfo(
    val id: UUID?,
    val spaceName: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime?,
    val itemListResponse: List<RentedItemList>,
) {
    companion object {
        fun from(entity: UsageHistory) =
            CurrentUserUsageInfo(
                id = entity.id,
                spaceName = entity.space.name,
                startAt = entity.startAt,
                endAt = entity.endAt,
                itemListResponse =
                    entity.rental.map {
                        RentedItemList(
                            id = it.id,
                            name = it.item.name,
                            quantity = it.quantity,
                            returnedQuantity = it.returnedQuantity,
                        )
                    },
            )
    }
}
