package org.aing.danurirest.domain.item.usecase

import org.aing.danurirest.domain.item.dto.ItemRentalRequest
import org.aing.danurirest.domain.item.dto.ItemRentalResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.item.entity.Item
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.aing.danurirest.persistence.rental.entity.Rental
import org.aing.danurirest.persistence.rental.repository.RentalJpaRepository
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RentItemUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val rentalJpaRepository: RentalJpaRepository,
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
) {
    @Transactional
    fun execute(request: ItemRentalRequest): ItemRentalResponse {
        val usage =
            usageHistoryJpaRepository
                .findById(
                    request.usageId,
                ).orElseThrow { CustomException(CustomErrorCode.NOT_USAGE_FOUND) }

        if (usage.endAt?.isAfter(LocalDateTime.now()) != true) {
            throw CustomException(CustomErrorCode.ALREADY_END)
        }

        val item =
            itemJpaRepository
                .findById(request.itemId)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

        validateItemAvailability(item, request.quantity)

        val rental = createRental(usage, item, request.quantity)
        val savedRental = rentalJpaRepository.save(rental)

        updateItemQuantity(item, request.quantity)

        return ItemRentalResponse(
            id = savedRental.id,
            itemId = item.id!!,
            itemName = item.name,
            quantity = request.quantity,
            borrowedAt = savedRental.borrowedAt,
            returnedQuantity = savedRental.returnedQuantity,
        )
    }

    private fun validateItemAvailability(
        item: Item,
        quantity: Int,
    ) {
        if (item.status != ItemStatus.AVAILABLE) {
            throw CustomException(CustomErrorCode.ITEM_NOT_AVAILABLE)
        }

        if (item.availableQuantity < quantity) {
            throw CustomException(CustomErrorCode.INSUFFICIENT_ITEM_QUANTITY)
        }
    }

    private fun createRental(
        usage: UsageHistory,
        item: Item,
        quantity: Int,
    ): Rental =
        Rental(
            item = item,
            usage = usage,
            quantity = quantity,
            borrowedAt = LocalDateTime.now(),
            returnedQuantity = 0,
        )

    private fun updateItemQuantity(
        item: Item,
        quantity: Int,
    ) {
        item.availableQuantity -= quantity
        if (item.availableQuantity == 0) {
            item.status = ItemStatus.NOT_AVAILABLE
        }
        itemJpaRepository.save(item)
    }
}
