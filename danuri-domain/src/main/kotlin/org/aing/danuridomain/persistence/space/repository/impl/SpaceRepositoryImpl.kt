package org.aing.danuridomain.persistence.space.repository.impl

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceJpaRepository
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class SpaceRepositoryImpl(
    private val spaceJpaRepository: SpaceJpaRepository,
) : SpaceRepository {
    override fun findById(spaceId: UUID): Optional<Space> = spaceJpaRepository.findById(spaceId)

    override fun existsById(spaceId: UUID): Boolean = spaceJpaRepository.existsById(spaceId)
}
