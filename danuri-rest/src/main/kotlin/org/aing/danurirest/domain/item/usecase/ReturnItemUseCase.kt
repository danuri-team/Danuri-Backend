package org.aing.danurirest.domain.item.usecase

import org.aing.danurirest.domain.item.dto.ItemReturnRequest
import org.aing.danurirest.domain.item.dto.ItemReturnResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.item.entity.Item
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.aing.danurirest.persistence.rental.RentalStatus
import org.aing.danurirest.persistence.rental.entity.Rental
import org.aing.danurirest.persistence.rental.repository.RentalJpaRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class ReturnItemUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val rentalJpaRepository: RentalJpaRepository,
) {
    fun execute(
        rentalId: UUID,
        request: ItemReturnRequest,
    ): ItemReturnResponse {
        val rental =
            rentalJpaRepository
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

        if (rental.quantity == rental.returnedQuantity) {
            rental.status = RentalStatus.RETURNED
        }

        rentalJpaRepository.save(rental)
    }

    private fun updateItemQuantity(
        item: Item,
        quantity: Int,
    ) {
        item.availableQuantity += quantity
        if (item.availableQuantity > 0) {
            item.status = ItemStatus.AVAILABLE
        }
        itemJpaRepository.save(item)
    }
}
