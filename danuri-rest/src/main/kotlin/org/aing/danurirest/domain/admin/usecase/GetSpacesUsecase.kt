package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.springframework.stereotype.Service

@Service
class GetSpacesUsecase(
    private val spaceRepository: SpaceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(): List<SpaceResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val spaces = spaceRepository.findByCompanyId(companyId)
        return spaces.map { SpaceResponse.from(it) }
    }
} 
