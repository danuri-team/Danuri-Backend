package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danuridomain.persistence.rental.dto.RentalResponse
import org.aing.danuridomain.persistence.rental.entity.Rental
import org.aing.danuridomain.persistence.rental.repository.RentalRepository
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.domain.admin.dto.CreateRentalRequest
import org.aing.danurirest.domain.admin.dto.UpdateRentalRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class RentalManagementUsecase(
    val rentalRepository: RentalRepository,
    val usageHistoryRepository: UsageHistoryRepository,
    val itemRepository: ItemRepository,
    val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun create(request: CreateRentalRequest) {
        // TODO: 회사 검증 필요
        val usageHistory =
            usageHistoryRepository.findById(request.usageId).orElseThrow {
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

        rentalRepository.save(
            Rental(
                item = item,
                usage = usageHistory,
                quantity = request.quantity,
                borrowedAt = LocalDateTime.now(),
            ),
        )
    }

    fun read(rentalId: UUID): RentalResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val rental =
            rentalRepository.findByIdAndCompanyId(rentalId, adminCompanyId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND)
            }
        return rental
    }

    fun readAll(): List<RentalResponse> {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        return rentalRepository.findByCompanyId(adminCompanyId)
    }

    fun update(
        request: UpdateRentalRequest,
        rentalId: UUID,
    ) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val rental: Rental =
            rentalRepository.findById(rentalId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND)
            }
        if (rental.item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val modifiedRental = rental.copy(quantity = request.quantity, returnedQuantity = request.returnedQuantity, status = request.status)
        rentalRepository.save(modifiedRental)
    }

    fun delete(rentalId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val rental = rentalRepository.findById(rentalId).orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND) }
        if (rental.item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }
        rentalRepository.delete(rentalId)
    }
}
