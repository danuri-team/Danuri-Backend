package org.aing.danurirest.domain.space.dto

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

data class SpaceUsingInfoResponse(
    val id: UUID?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime? = null,
    val rental: List<RentalSimpleResponse>,
) {
    companion object {
        fun from(entity: UsageHistory): SpaceUsingInfoResponse =
            SpaceUsingInfoResponse(
                id = entity.id,
                startAt = entity.start_at,
                endAt = entity.end_at,
                rental = entity.rental.map { RentalSimpleResponse.from(it) },
            )
    }
}
