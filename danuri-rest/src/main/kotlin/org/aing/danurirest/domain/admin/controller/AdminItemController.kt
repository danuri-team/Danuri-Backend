package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.ItemRequest
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.admin.usecase.CreateItemUsecase
import org.aing.danurirest.domain.admin.usecase.DeleteItemUsecase
import org.aing.danurirest.domain.admin.usecase.GetItemUsecase
import org.aing.danurirest.domain.admin.usecase.GetItemsUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateItemUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/items")
class AdminItemController(
    private val createItemUsecase: CreateItemUsecase,
    private val updateItemUsecase: UpdateItemUsecase,
    private val deleteItemUsecase: DeleteItemUsecase,
    private val getItemUsecase: GetItemUsecase,
    private val getCompanyItemsUsecase: GetItemsUsecase,
) {
    @PostMapping
    fun createItem(
        @Valid @RequestBody request: ItemRequest,
    ): ResponseEntity<ItemResponse> =
        createItemUsecase.execute(request).run {
            ResponseEntity.ok(this)
        }

    @PutMapping("/{itemId}")
    fun updateItem(
        @PathVariable itemId: UUID,
        @Valid @RequestBody request: ItemRequest,
    ): ResponseEntity<ItemResponse> =
        updateItemUsecase.execute(itemId, request).run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping("/{itemId}")
    fun deleteItem(
        @PathVariable itemId: UUID,
    ): ResponseEntity<Unit> =
        deleteItemUsecase.execute(itemId).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping("/{itemId}")
    fun getItem(
        @PathVariable itemId: UUID,
    ): ResponseEntity<ItemResponse> =
        getItemUsecase.execute(itemId).run {
            ResponseEntity.ok(this)
        }

    @GetMapping
    fun getCurrentCompanyItems(): ResponseEntity<List<ItemResponse>> =
        getCompanyItemsUsecase.execute().run {
            ResponseEntity.ok(this)
        }
}
