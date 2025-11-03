package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.persistence.rental.dto.RentalResponse
import org.aing.danurirest.persistence.rental.repository.RentalRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetRentalsUsecase(
    private val rentalRepository: RentalRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(pageable: Pageable): Page<RentalResponse> {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        return rentalRepository.findByCompanyId(adminCompanyId, pageable)
    }
}
