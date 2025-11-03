package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteSpaceUsecase(
    private val spaceJpaRepository: SpaceJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(spaceId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceJpaRepository.findByIdAndCompanyId(spaceId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_SPACE)

        spaceJpaRepository.delete(space)
    }
}
