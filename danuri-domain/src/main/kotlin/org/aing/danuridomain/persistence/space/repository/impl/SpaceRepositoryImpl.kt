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

    override fun save(space: Space): Space = spaceJpaRepository.save(space)
    
    override fun findByCompanyId(companyId: UUID): List<Space> = spaceJpaRepository.findAllByCompanyId(companyId)
    
    override fun delete(spaceId: UUID) {
        spaceJpaRepository.deleteById(spaceId)
    }
    
    override fun update(space: Space): Space = spaceJpaRepository.save(space)
}
