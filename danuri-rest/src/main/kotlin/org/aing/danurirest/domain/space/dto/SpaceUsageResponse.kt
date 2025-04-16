package org.aing.danurirest.domain.space.dto

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

data class SpaceUsageResponse(
    val id: UUID?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime? = null,
) {
    companion object {
        fun from(entity: UsageHistory): SpaceUsageResponse =
            SpaceUsageResponse(
                id = entity.id,
                startAt = entity.startAt,
                endAt = entity.endAt,
            )
    }
}
