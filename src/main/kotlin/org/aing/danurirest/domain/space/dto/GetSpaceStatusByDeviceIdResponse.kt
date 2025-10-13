package org.aing.danurirest.domain.space.dto

import org.aing.danurirest.persistence.space.entity.Space
import java.util.UUID

data class GetSpaceStatusByDeviceIdResponse(
    val spaceId: UUID?,
    val name: String,
    val isAvailable: Boolean,
    val timeSlots: List<SpaceTimeSlot>,
) {
    companion object {
        fun from(
            entity: Space,
            isSpaceAvailable: Boolean,
            timeSlots: List<SpaceTimeSlot>,
        ) = GetSpaceStatusByDeviceIdResponse(
            spaceId = entity.id!!,
            name = entity.name,
            isAvailable = isSpaceAvailable,
            timeSlots = timeSlots,
        )
    }
}
