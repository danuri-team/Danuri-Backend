package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.admin.dto.ItemUpdateRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdateItemUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        itemId: UUID,
        request: ItemUpdateRequest,
    ): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val item =
            itemJpaRepository
                .findById(itemId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

        if (item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        item.name = request.name
        item.totalQuantity = request.totalQuantity
        item.availableQuantity = request.availableQuantity
        item.status = request.status

        return ItemResponse.from(itemJpaRepository.save(item))
    }
}
