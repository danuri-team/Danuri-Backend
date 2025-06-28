package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.springframework.stereotype.Service

@Service
class GetItemsUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(): List<ItemResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val items = itemJpaRepository.findAllByCompanyId(companyId)
        return items.map { ItemResponse.from(it) }
    }
}
