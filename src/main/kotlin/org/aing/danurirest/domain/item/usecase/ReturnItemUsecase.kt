package org.aing.danurirest.domain.item.usecase

import org.aing.danurirest.domain.common.dto.QrUsageIdRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.aing.danurirest.persistence.rental.repository.RentalJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReturnItemUsecase(
    private val rentalJpaRepository: RentalJpaRepository,
    private val itemJpaRepository: ItemJpaRepository,
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
) {
    @Transactional
    fun execute(request: QrUsageIdRequest) {
        usageHistoryJpaRepository
            .findById(request.usageId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND) }

        val rentals =
            rentalJpaRepository
                .findAllByUsageId(request.usageId)
                .takeIf { it.isNotEmpty() }
                ?: throw CustomException(CustomErrorCode.NOT_RENTED_ITEM)

        if (rentals.all { it.returnedAt != null }) {
            throw CustomException(CustomErrorCode.ALREADY_RETURNED_ITEM)
        }

        val now = LocalDateTime.now()

        rentals.forEach { rental ->
            if (rental.returnedAt != null) return@forEach

            val item =
                itemJpaRepository
                    .findById(rental.item.id!!)
                    .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

            item.availableQuantity += 1

            if (item.availableQuantity > 0) {
                item.status = ItemStatus.AVAILABLE
            }

            rental.returnedQuantity = rental.quantity
            rental.returnedAt = now
        }
    }
}
