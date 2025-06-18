package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetItemUsecase(
    private val itemRepository: ItemRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(itemId: UUID): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val item =
            itemRepository
                .findById(itemId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

        if (item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        return ItemResponse.from(item)
    }
} 