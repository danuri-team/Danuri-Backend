package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UpdateRentalRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.rental.entity.Rental
import org.aing.danurirest.persistence.rental.repository.RentalJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdateRentalUsecase(
    private val rentalJpaRepository: RentalJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(
        request: UpdateRentalRequest,
        rentalId: UUID,
    ) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val rental: Rental =
            rentalJpaRepository.findById(rentalId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND)
            }
        if (rental.item.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        rental.quantity = request.quantity
        rental.returnedQuantity = request.returnedQuantity
        rental.status = request.status
    }
} 
