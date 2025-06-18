package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danurirest.domain.admin.dto.ItemRequest
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdateItemUsecase(
    private val itemRepository: ItemRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        itemId: UUID,
        request: ItemRequest,
    ): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val item =
            itemRepository
                .findById(itemId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

        if (item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val availableQuantityDiff = request.totalQuantity - item.totalQuantity
        val updatedAvailableQuantity = item.availableQuantity + availableQuantityDiff

        val updatedItem =
            Item(
                id = item.id,
                company = item.company,
                name = request.name,
                totalQuantity = request.totalQuantity,
                availableQuantity = updatedAvailableQuantity,
                status = request.status,
            )

        return ItemResponse.from(itemRepository.update(updatedItem))
    }
} 