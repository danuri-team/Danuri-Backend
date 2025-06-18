package org.aing.danuridomain.persistence.space.repository

import org.aing.danuridomain.persistence.space.dto.SpaceAvailabilityDto
import org.aing.danuridomain.persistence.space.entity.Space
import java.util.Optional
import java.util.UUID

interface SpaceRepository {
    fun findById(spaceId: UUID): Optional<Space>

    fun save(space: Space): Space

    fun findByCompanyId(companyId: UUID): List<Space>

    fun delete(entity: Space)

    fun findSpacesWithAvailabilityByDeviceId(deviceId: UUID): List<SpaceAvailabilityDto>
}
