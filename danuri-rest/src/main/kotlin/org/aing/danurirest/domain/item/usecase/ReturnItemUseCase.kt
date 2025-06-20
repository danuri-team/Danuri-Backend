package org.aing.danurirest.domain.item.usecase

import org.aing.danuridomain.persistence.item.ItemStatus
import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danuridomain.persistence.rental.RentalStatus
import org.aing.danuridomain.persistence.rental.entity.Rental
import org.aing.danuridomain.persistence.rental.repository.RentalRepository
import org.aing.danurirest.domain.item.dto.ItemReturnRequest
import org.aing.danurirest.domain.item.dto.ItemReturnResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class ReturnItemUsecase(
    private val itemRepository: ItemRepository,
    private val rentalRepository: RentalRepository,
) {
    fun execute(
        rentalId: UUID,
        request: ItemReturnRequest,
    ): ItemReturnResponse {
        val rental =
            rentalRepository
                .findById(rentalId)
                .orElseThrow { CustomException(CustomErrorCode.NO_OWN_SPACE_OR_AVAILABLE) }

        validateReturnRequest(rental, request.quantity)

        val item = rental.item
        updateRental(rental, request.quantity)
        updateItemQuantity(item, request.quantity)

        return ItemReturnResponse(
            id = rental.id,
            itemId = item.id!!,
            itemName = item.name,
            totalQuantity = rental.quantity,
            returnedQuantity = rental.returnedQuantity,
            returnedAt = rental.returnedAt!!,
        )
    }

    private fun validateReturnRequest(
        rental: Rental,
        quantity: Int,
    ) {
        if (rental.returnedAt != null) {
            throw CustomException(CustomErrorCode.ALREADY_RETURNED)
        }

        if (rental.quantity < rental.returnedQuantity + quantity) {
            throw CustomException(CustomErrorCode.OVER_QUANTITY)
        }
    }

    private fun updateRental(
        rental: Rental,
        quantity: Int,
    ) {
        rental.returnedQuantity += quantity
        rental.returnedAt = LocalDateTime.now()
        rental.status = RentalStatus.RETURNED
        rentalRepository.save(rental)
    }

    private fun updateItemQuantity(
        item: Item,
        quantity: Int,
    ) {
        item.availableQuantity += quantity
        if (item.availableQuantity > 0) {
            item.status = ItemStatus.AVAILABLE
        }
        itemRepository.save(item)
    }
}
