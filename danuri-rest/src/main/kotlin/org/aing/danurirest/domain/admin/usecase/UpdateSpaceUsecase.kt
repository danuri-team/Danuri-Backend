package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.admin.dto.SpaceRequest
import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdateSpaceUsecase(
    private val spaceRepository: SpaceRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        spaceId: UUID,
        request: SpaceRequest,
    ): SpaceResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val updatedSpace =
            Space(
                id = space.id,
                company = space.company,
                name = request.name,
                startAt = request.startAt,
                endAt = request.endAt,
                usage = space.usage,
            )

        return SpaceResponse.from(spaceRepository.save(updatedSpace))
    }
} 