package org.aing.danuridomain.persistence.space.repository

import org.aing.danuridomain.persistence.space.entity.Space
import java.util.Optional
import java.util.UUID

interface SpaceRepository {
    fun findById(spaceId: UUID): Optional<Space>

    fun existsById(spaceId: UUID): Boolean

    fun save(space: Space): Space

    fun findByCompanyId(companyId: UUID): List<Space>

    fun delete(spaceId: UUID)

    fun update(space: Space): Space
}
