package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danurirest.domain.admin.dto.ItemRequest
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateItemUsecase(
    private val itemRepository: ItemRepository,
    private val companyRepository: CompanyRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: ItemRequest): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyRepository
                .findById(adminCompanyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val item =
            Item(
                company = company,
                name = request.name,
                totalQuantity = request.totalQuantity,
                availableQuantity = request.totalQuantity,
                status = request.status,
            )

        return ItemResponse.from(itemRepository.save(item))
    }
} 
