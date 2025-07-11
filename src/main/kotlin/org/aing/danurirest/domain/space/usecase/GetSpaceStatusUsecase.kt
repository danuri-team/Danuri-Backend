package org.aing.danurirest.domain.space.usecase

import org.aing.danurirest.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.space.dto.GetSpaceStatusByDeviceIdResponse
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class GetSpaceStatusUsecase(
    private val spaceRepository: SpaceRepository,
) {
    fun execute(): List<GetSpaceStatusByDeviceIdResponse> {
        val deviceContextDto: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        return spaceRepository.findSpacesWithAvailabilityByDeviceId(deviceContextDto.id!!).map {
            GetSpaceStatusByDeviceIdResponse.fromDomainDto(it)
        }
    }
}
