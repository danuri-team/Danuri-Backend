package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.CreateRentalRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.aing.danurirest.persistence.rental.RentalStatus
import org.aing.danurirest.persistence.rental.entity.Rental
import org.aing.danurirest.persistence.rental.repository.RentalJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class CreateRentalUsecase(
    private val rentalJpaRepository: RentalJpaRepository,
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val itemRepository: ItemJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: CreateRentalRequest) {
        val usageHistory =
            usageHistoryJpaRepository.findById(request.usageId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_USAGE_FOUND)
            }

        usageHistory.endAt?.let {
            if (it < LocalDateTime.now()) {
                throw CustomException(CustomErrorCode.USAGE_EXPIRED)
            }
        }

        val item =
            itemRepository
                .findById(request.itemId)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_ITEM) }

        if (item.status == ItemStatus.NOT_AVAILABLE || item.availableQuantity < request.quantity) {
            throw CustomException(CustomErrorCode.ITEM_NOT_AVAILABLE)
        }

        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        if (item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        rentalJpaRepository.save(
            Rental(
                item = item,
                usage = usageHistory,
                quantity = request.quantity,
                borrowedAt = LocalDateTime.now(),
                status = RentalStatus.NOT_CONFIRMED,
            ),
        )
    }
}
