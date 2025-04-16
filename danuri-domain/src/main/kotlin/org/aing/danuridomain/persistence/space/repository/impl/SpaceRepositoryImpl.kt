package org.aing.danuridomain.persistence.space.repository.impl

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceJpaRepository
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Repository
class SpaceRepositoryImpl(
    private val spaceJpaRepository: SpaceJpaRepository,
) : SpaceRepository {
    override fun getAvailableSpace(
        companyId: UUID,
        targetTime: LocalTime,
        targetDateTime: LocalDateTime,
    ): List<Space> =
        spaceJpaRepository.findAvailableSpaces(
            companyId = companyId,
            targetTime = targetTime,
            targetDateTime = targetDateTime,
        )
}
