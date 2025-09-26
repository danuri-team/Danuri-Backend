package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UpdateRentalRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.rental.repository.RentalJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateRentalUsecase(
    private val rentalJpaRepository: RentalJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional
    fun execute(
        request: UpdateRentalRequest,
        rentalId: UUID,
    ) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val rental =
            rentalJpaRepository.findByIdAndCompanyId(rentalId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND)

        rental.quantity = request.quantity
        rental.returnedQuantity = request.returnedQuantity
        rental.status = request.status
    }
}
