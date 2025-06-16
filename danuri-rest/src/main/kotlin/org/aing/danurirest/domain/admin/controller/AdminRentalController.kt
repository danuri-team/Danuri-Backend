package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danuridomain.persistence.rental.dto.RentalResponse
import org.aing.danurirest.domain.admin.dto.CreateRentalRequest
import org.aing.danurirest.domain.admin.dto.UpdateRentalRequest
import org.aing.danurirest.domain.admin.usecase.RentalManagementUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/admin/rentals")
class AdminRentalController(
    private val rentalManagementUsecase: RentalManagementUsecase,
) {
    @PostMapping
    fun createRental(
        @Valid @RequestBody request: CreateRentalRequest,
    ): ResponseEntity<Unit> =
        rentalManagementUsecase
            .create(request)
            .run { ResponseEntity.noContent().build() }

    @GetMapping("/{rentalId}")
    fun getRental(
        @PathVariable rentalId: UUID,
    ): ResponseEntity<RentalResponse> = rentalManagementUsecase.read(rentalId).run { ResponseEntity.ok(this) }

    @GetMapping
    fun getAllRentals(): ResponseEntity<List<RentalResponse>> = rentalManagementUsecase.readAll().run { ResponseEntity.ok(this) }

    @PutMapping("/{rentalId}")
    fun updateRental(
        @PathVariable rentalId: UUID,
        @RequestBody request: UpdateRentalRequest,
    ): ResponseEntity<Unit> = rentalManagementUsecase.update(request, rentalId).run { ResponseEntity.noContent().build() }

    @DeleteMapping("/{rentalId}")
    fun deleteRental(
        @PathVariable rentalId: UUID,
    ): ResponseEntity<Unit> = rentalManagementUsecase.delete(rentalId).run { ResponseEntity.noContent().build() }
}
