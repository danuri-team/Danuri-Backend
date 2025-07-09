package org.aing.danurirest.domain.admin.dto

import org.aing.danurirest.persistence.space.entity.Space
import java.time.LocalTime
import java.util.UUID

data class SpaceResponse(
    val id: UUID,
    val name: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val usageCount: Int,
) {
    companion object {
        fun from(entity: Space): SpaceResponse =
            SpaceResponse(
                id = entity.id!!,
                name = entity.name,
                startAt = entity.startAt,
                endAt = entity.endAt,
                usageCount = entity.usage.size,
            )
    }
}
