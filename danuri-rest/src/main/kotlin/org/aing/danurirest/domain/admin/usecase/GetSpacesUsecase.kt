package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.springframework.stereotype.Service

@Service
class GetSpacesUsecase(
    private val spaceJpaRepository: SpaceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(): List<SpaceResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val spaces = spaceJpaRepository.findAllByCompanyId(companyId)
        return spaces.map { SpaceResponse.from(it) }
    }
}
