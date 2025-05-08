package org.aing.danurirest.domain.space.dto

import org.aing.danuridomain.persistence.space.dto.SpaceAvailabilityDto
import java.time.LocalTime
import java.util.UUID

data class GetSpaceStatusByDeviceIdResponse(
    val id: UUID?,
    val name: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val isAvailable: Boolean,
) {
    companion object {
        fun fromDomainDto(dto: SpaceAvailabilityDto): GetSpaceStatusByDeviceIdResponse =
            GetSpaceStatusByDeviceIdResponse(
                id = dto.space.id,
                name = dto.space.name,
                startAt = dto.space.startAt,
                endAt = dto.space.endAt,
                isAvailable = dto.isAvailable,
            )
    }
}
