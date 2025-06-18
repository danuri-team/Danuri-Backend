package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.springframework.stereotype.Service

@Service
class GetItemsUsecase(
    private val itemRepository: ItemRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(): List<ItemResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val items = itemRepository.findByCompanyId(companyId)
        return items.map { ItemResponse.from(it) }
    }
} 
