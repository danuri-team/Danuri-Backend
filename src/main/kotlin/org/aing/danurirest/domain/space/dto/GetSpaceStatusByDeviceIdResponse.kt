package org.aing.danurirest.domain.space.dto

import org.aing.danurirest.persistence.space.dto.SpaceAvailabilityDto
import java.time.LocalTime
import java.util.UUID

data class GetSpaceStatusByDeviceIdResponse(
    val spaceId: UUID?,
    val name: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val isAvailable: Boolean,
) {
    companion object {
        fun fromDomainDto(dto: SpaceAvailabilityDto): GetSpaceStatusByDeviceIdResponse =
            GetSpaceStatusByDeviceIdResponse(
                spaceId = dto.space.id,
                name = dto.space.name,
                startAt = dto.space.startAt,
                endAt = dto.space.endAt,
                isAvailable = dto.isAvailable,
            )
    }
}
