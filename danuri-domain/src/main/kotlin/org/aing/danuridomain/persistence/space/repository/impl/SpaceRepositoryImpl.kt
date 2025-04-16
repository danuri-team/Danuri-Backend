package org.aing.danuridomain.persistence.space.repository.impl

import org.aing.danuridomain.persistence.space.repository.SpaceJpaRepository
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.springframework.stereotype.Repository

@Repository
class SpaceRepositoryImpl(
    private val spaceJpaRepository: SpaceJpaRepository,
) : SpaceRepository
