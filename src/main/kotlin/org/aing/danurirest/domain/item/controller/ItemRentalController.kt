package org.aing.danurirest.domain.item.controller

import org.aing.danurirest.domain.common.dto.QrUsageIdRequest
import org.aing.danurirest.domain.item.dto.ItemListResponse
import org.aing.danurirest.domain.item.dto.ItemRentalRequest
import org.aing.danurirest.domain.item.dto.ItemRentalResponse
import org.aing.danurirest.domain.item.usecase.GetItemListUsecase
import org.aing.danurirest.domain.item.usecase.RentItemUsecase
import org.aing.danurirest.domain.item.usecase.ReturnItemUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/item")
class ItemRentalController(
    private val rentItemUseCase: RentItemUsecase,
    private val returnItemUseCase: ReturnItemUsecase,
    private val getItemListUsecase: GetItemListUsecase,
) {
    @PostMapping
    fun rentItem(
        @RequestBody request: ItemRentalRequest,
    ): ResponseEntity<ItemRentalResponse> =
        rentItemUseCase.execute(request).run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping("/return")
    fun returnItem(
        @RequestBody request: QrUsageIdRequest,
    ): ResponseEntity<Unit> =
        returnItemUseCase.execute(request).run {
            ResponseEntity.ok(this)
        }

    @GetMapping
    fun getItemList(): ResponseEntity<List<ItemListResponse>> =
        getItemListUsecase.execute().run {
            ResponseEntity.ok(this)
        }
}
