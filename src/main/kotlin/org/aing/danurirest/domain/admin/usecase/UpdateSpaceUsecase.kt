package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.SpaceRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdateSpaceUsecase(
    private val spaceJpaRepository: SpaceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        spaceId: UUID,
        request: SpaceRequest,
    ) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceJpaRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        space.startAt = request.startAt
        space.name = request.name
        space.endAt = request.endAt

        spaceJpaRepository.save(space)
    }
}
