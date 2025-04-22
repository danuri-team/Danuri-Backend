package org.aing.danurirest.domain.item.controller

import org.aing.danurirest.domain.item.dto.ItemRentalRequest
import org.aing.danurirest.domain.item.dto.ItemRentalResponse
import org.aing.danurirest.domain.item.dto.ItemReturnRequest
import org.aing.danurirest.domain.item.dto.ItemReturnResponse
import org.aing.danurirest.domain.item.usecase.RentItemUseCase
import org.aing.danurirest.domain.item.usecase.ReturnItemUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/item")
class ItemRentalController(
    private val rentItemUseCase: RentItemUseCase,
    private val returnItemUseCase: ReturnItemUseCase,
) {
    @PostMapping("/{usageId}/rental")
    fun rentItem(
        @PathVariable usageId: UUID,
        @RequestBody request: ItemRentalRequest,
    ): ResponseEntity<ItemRentalResponse> =
        rentItemUseCase.execute(usageId, request).run {
            ResponseEntity.ok(this)
        }

    @PostMapping("/{rentalId}/return")
    fun returnItem(
        @PathVariable rentalId: UUID,
        @RequestBody request: ItemReturnRequest,
    ): ResponseEntity<ItemReturnResponse> =
        returnItemUseCase.execute(rentalId, request).run {
            ResponseEntity.ok(this)
        }
}
