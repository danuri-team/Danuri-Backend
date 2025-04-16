package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetAvailableSpaceUsecase(
    private val spaceRepository: SpaceRepository,
) {
    fun execute(companyId: UUID): List<Space> =
        spaceRepository.getAvailableSpace(
            companyId = companyId,
        )
}
