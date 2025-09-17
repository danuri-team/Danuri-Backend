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
class UpdateSpaceUsecase(
    private val spaceJpaRepository: SpaceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional
    fun execute(
        spaceId: UUID,
        request: SpaceRequest,
    ) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceJpaRepository.findByIdAndCompanyId(spaceId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_SPACE)

        space.startAt = request.startAt
        space.name = request.name
        space.endAt = request.endAt

        spaceJpaRepository.save(space)
    }
}
