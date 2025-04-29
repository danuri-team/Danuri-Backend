package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danurirest.domain.admin.dto.ItemRequest
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ItemManagementUsecase(
    private val itemRepository: ItemRepository,
    private val companyRepository: CompanyRepository
) {
    fun createItem(request: ItemRequest): ItemResponse {
        val company = companyRepository.findById(request.companyId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        
        val item = Item(
            company = company,
            name = request.name,
            total_quantity = request.totalQuantity,
            available_quantity = request.totalQuantity,
            status = request.status
        )
        
        return ItemResponse.from(itemRepository.save(item))
    }
    
    fun updateItem(itemId: UUID, request: ItemRequest): ItemResponse {
        val item = itemRepository.findById(itemId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }
        
        // 회사가 변경되었는지 확인
        val company = if (item.company.id != request.companyId) {
            companyRepository.findById(request.companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
        } else {
            item.company
        }
        
        // 가용 수량 조정
        val availableQuantityDiff = request.totalQuantity - item.total_quantity
        val updatedAvailableQuantity = item.available_quantity + availableQuantityDiff
        
        val updatedItem = Item(
            id = item.id,
            company = company,
            name = request.name,
            total_quantity = request.totalQuantity,
            available_quantity = updatedAvailableQuantity,
            status = request.status
        )
        
        return ItemResponse.from(itemRepository.update(updatedItem))
    }
    
    fun deleteItem(itemId: UUID) {
        val item = itemRepository.findById(itemId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }
        
        // 대여 중인 품목이 있는지 확인
        if (item.available_quantity < item.total_quantity) {
            throw CustomException(CustomErrorCode.ITEM_IN_USE)
        }
        
        itemRepository.delete(itemId)
    }
    
    fun getItem(itemId: UUID): ItemResponse {
        val item = itemRepository.findById(itemId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ITEM) }
        
        return ItemResponse.from(item)
    }
    
    fun getItemsByCompany(companyId: UUID): List<ItemResponse> {
        val items = itemRepository.findByCompanyId(companyId)
        return items.map { ItemResponse.from(it) }
    }
} 