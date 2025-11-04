package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteItemUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(itemId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val item =
            itemJpaRepository.findByIdAndCompanyId(itemId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_ITEM)

        itemJpaRepository.delete(item)
    }
}
