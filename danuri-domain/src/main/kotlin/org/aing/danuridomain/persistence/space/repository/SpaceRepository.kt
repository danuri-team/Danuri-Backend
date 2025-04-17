package org.aing.danuridomain.persistence.space.repository

import org.aing.danuridomain.persistence.space.entity.Space
import java.util.Optional
import java.util.UUID

interface SpaceRepository {
    fun findById(spaceId: UUID): Optional<Space>
}
