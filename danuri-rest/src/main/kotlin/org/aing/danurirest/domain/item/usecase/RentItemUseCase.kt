package org.aing.danurirest.domain.item.usecase

import org.aing.danuridomain.persistence.item.ItemStatus
import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danuridomain.persistence.rental.entity.Rental
import org.aing.danuridomain.persistence.rental.repository.RentalRepository
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.domain.item.dto.ItemRentalRequest
import org.aing.danurirest.domain.item.dto.ItemRentalResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class RentItemUsecase(
    private val itemRepository: ItemRepository,
    private val rentalRepository: RentalRepository,
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(
        usageId: UUID,
        request: ItemRentalRequest,
    ): ItemRentalResponse {
        val user: ContextDto =
            SecurityContextHolder
                .getContext()
                .authentication.principal
                as ContextDto

        val usage =
            usageHistoryRepository
                .spaceUsingInfo(
                    usageId = usageId,
                    userId = user.id!!,
                ).orElseThrow { CustomException(CustomErrorCode.NO_OWN_SPACE_OR_AVAILABLE) }

        if (usage.endAt?.isAfter(LocalDateTime.now()) != true) {
            throw CustomException(CustomErrorCode.ALREADY_END)
        }

        val item =
            itemRepository
                .findById(request.itemId)
                .orElseThrow { IllegalArgumentException("존재하지 않는 아이템입니다.") }

        validateItemAvailability(item, request.quantity)

        val rental = createRental(usage, item, request.quantity)
        val savedRental = rentalRepository.save(rental)

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
        itemRepository.save(item)
    }
}
