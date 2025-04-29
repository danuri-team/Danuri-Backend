package org.aing.danurirest.domain.admin.dto

import org.aing.danuridomain.persistence.space.entity.Space
import java.time.LocalTime
import java.util.UUID

data class SpaceResponse(
    val id: UUID,
    val companyId: UUID,
    val companyName: String,
    val name: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val usageCount: Int
) {
    companion object {
        fun from(entity: Space): SpaceResponse =
            SpaceResponse(
                id = entity.id!!,
                companyId = entity.company.id!!,
                companyName = entity.company.name,
                name = entity.name,
                startAt = entity.start_at,
                endAt = entity.end_at,
                usageCount = entity.usage.size
            )
    }
} 