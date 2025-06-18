package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.admin.dto.SpaceRequest
import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service

@Service
class CreateSpaceUsecase(
    private val spaceRepository: SpaceRepository,
    private val companyRepository: CompanyRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: SpaceRequest): SpaceResponse {
        val companyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyRepository
                .findById(companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val space =
            Space(
                company = company,
                name = request.name,
                startAt = request.startAt,
                endAt = request.endAt,
            )

        return SpaceResponse.from(spaceRepository.save(space))
    }
} 