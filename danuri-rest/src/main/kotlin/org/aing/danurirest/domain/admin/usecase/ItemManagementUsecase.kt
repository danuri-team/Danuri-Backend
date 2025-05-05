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
import java.util.UUID

@Service
class ItemManagementUsecase(
    private val itemRepository: ItemRepository,
    private val companyRepository: CompanyRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun createItem(request: ItemRequest): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyRepository
                .findById(adminCompanyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val item =
            Item(
                company = company,
                name = request.name,
                total_quantity = request.totalQuantity,
                available_quantity = request.totalQuantity,
                status = request.status,
            )

        return ItemResponse.from(itemRepository.save(item))
    }

    fun updateItem(
        itemId: UUID,
        request: ItemRequest,
    ): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        // TODO: 본인 회사의 아이템만 업데이트가 가능해야 함

        val item =
            itemRepository
                .findById(itemId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

        val availableQuantityDiff = request.totalQuantity - item.total_quantity
        val updatedAvailableQuantity = item.available_quantity + availableQuantityDiff

        val updatedItem =
            Item(
                id = item.id,
                company = item.company,
                name = request.name,
                total_quantity = request.totalQuantity,
                available_quantity = updatedAvailableQuantity,
                status = request.status,
            )

        return ItemResponse.from(itemRepository.update(updatedItem))
    }

    fun deleteItem(itemId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val item =
            itemRepository
                .findById(itemId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

        if (item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        itemRepository.delete(itemId)
    }

    fun getItem(itemId: UUID): ItemResponse {
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

    fun getItemsByCompany(companyId: UUID): List<ItemResponse> {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        if (companyId != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val items = itemRepository.findByCompanyId(companyId)
        return items.map { ItemResponse.from(it) }
    }

    fun getCurrentAdminCompanyItems(): List<ItemResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val items = itemRepository.findByCompanyId(companyId)
        return items.map { ItemResponse.from(it) }
    }
} 
