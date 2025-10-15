package org.aing.danurirest.persistence.space.repository

import org.aing.danurirest.persistence.space.dto.SpaceWithBookingsDto
import java.time.LocalDate
import java.util.UUID

interface SpaceRepository {
    fun findSpacesWithBookingsByDeviceId(
        deviceId: UUID,
        date: LocalDate = LocalDate.now(),
    ): List<SpaceWithBookingsDto>
}
