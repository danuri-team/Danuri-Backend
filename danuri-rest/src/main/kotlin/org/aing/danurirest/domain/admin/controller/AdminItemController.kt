package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.ItemRequest
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.admin.usecase.ItemManagementUsecase
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
    private val itemManagementUsecase: ItemManagementUsecase,
) {
    @PostMapping
    fun createItem(
        @Valid @RequestBody request: ItemRequest,
    ): ResponseEntity<ItemResponse> =
        itemManagementUsecase.createItem(request).run {
            ResponseEntity.ok(this)
        }

    @PutMapping("/{itemId}")
    fun updateItem(
        @PathVariable itemId: UUID,
        @Valid @RequestBody request: ItemRequest,
    ): ResponseEntity<ItemResponse> =
        itemManagementUsecase.updateItem(itemId, request).run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping("/{itemId}")
    fun deleteItem(
        @PathVariable itemId: UUID,
    ): ResponseEntity<Unit> =
        itemManagementUsecase.deleteItem(itemId).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping("/{itemId}")
    fun getItem(
        @PathVariable itemId: UUID,
    ): ResponseEntity<ItemResponse> =
        itemManagementUsecase.getItem(itemId).run {
            ResponseEntity.ok(this)
        }

    @GetMapping
    fun getCurrentCompanyItems(): ResponseEntity<List<ItemResponse>> =
        itemManagementUsecase.getCurrentAdminCompanyItems().run {
            ResponseEntity.ok(this)
        }
}
