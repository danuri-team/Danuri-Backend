package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.CreateRentalRequest
import org.aing.danurirest.domain.admin.dto.UpdateRentalRequest
import org.aing.danurirest.domain.admin.usecase.CreateRentalUsecase
import org.aing.danurirest.domain.admin.usecase.DeleteRentalUsecase
import org.aing.danurirest.domain.admin.usecase.GetRentalUsecase
import org.aing.danurirest.domain.admin.usecase.GetRentalsUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateRentalUsecase
import org.aing.danurirest.persistence.rental.dto.RentalResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/admin/rentals")
class AdminRentalController(
    private val createRentalUsecase: CreateRentalUsecase,
    private val getRentalUsecase: GetRentalUsecase,
    private val getAllRentalsUsecase: GetRentalsUsecase,
    private val updateRentalUsecase: UpdateRentalUsecase,
    private val deleteRentalUsecase: DeleteRentalUsecase,
) {
    @PostMapping
    fun createRental(
        @Valid @RequestBody request: CreateRentalRequest,
    ): ResponseEntity<Unit> =
        createRentalUsecase
            .execute(request)
            .run { ResponseEntity.noContent().build() }

    @GetMapping("/{rentalId}")
    fun getRental(
        @PathVariable rentalId: UUID,
    ): ResponseEntity<RentalResponse> = getRentalUsecase.execute(rentalId).run { ResponseEntity.ok(this) }

    @GetMapping
    fun getAllRentals(
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<RentalResponse>> = getAllRentalsUsecase.execute(pageable).run { ResponseEntity.ok(this) }

    @PutMapping("/{rentalId}")
    fun updateRental(
        @PathVariable rentalId: UUID,
        @Valid @RequestBody request: UpdateRentalRequest,
    ): ResponseEntity<Unit> = updateRentalUsecase.execute(request, rentalId).run { ResponseEntity.noContent().build() }

    @DeleteMapping("/{rentalId}")
    fun deleteRental(
        @PathVariable rentalId: UUID,
    ): ResponseEntity<Unit> = deleteRentalUsecase.execute(rentalId).run { ResponseEntity.noContent().build() }
}
