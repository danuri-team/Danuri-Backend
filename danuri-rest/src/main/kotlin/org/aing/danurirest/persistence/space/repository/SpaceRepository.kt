package org.aing.danurirest.persistence.space.repository

import org.aing.danurirest.persistence.space.dto.SpaceAvailabilityDto
import java.util.UUID

interface SpaceRepository {
    fun findSpacesWithAvailabilityByDeviceId(deviceId: UUID): List<SpaceAvailabilityDto>
}
