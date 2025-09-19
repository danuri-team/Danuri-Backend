package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetItemUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(itemId: UUID): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val item =
            itemJpaRepository.findByIdAndCompanyId(itemId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_ITEM)

        return ItemResponse.from(item)
    }
}
