package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.SpaceRequest
import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.admin.usecase.SpaceManagementUsecase
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
@RequestMapping("/admin/spaces")
class AdminSpaceController(
    private val spaceManagementUsecase: SpaceManagementUsecase
) {
    @PostMapping
    fun createSpace(@Valid @RequestBody request: SpaceRequest): ResponseEntity<SpaceResponse> =
        spaceManagementUsecase.createSpace(request).run {
            ResponseEntity.ok(this)
        }
    
    @PutMapping("/{spaceId}")
    fun updateSpace(
        @PathVariable spaceId: UUID,
        @Valid @RequestBody request: SpaceRequest
    ): ResponseEntity<SpaceResponse> =
        spaceManagementUsecase.updateSpace(spaceId, request).run {
            ResponseEntity.ok(this)
        }
    
    @DeleteMapping("/{spaceId}")
    fun deleteSpace(@PathVariable spaceId: UUID): ResponseEntity<Unit> =
        spaceManagementUsecase.deleteSpace(spaceId).run {
            ResponseEntity.noContent().build()
        }
    
    @GetMapping("/{spaceId}")
    fun getSpace(@PathVariable spaceId: UUID): ResponseEntity<SpaceResponse> =
        spaceManagementUsecase.getSpace(spaceId).run {
            ResponseEntity.ok(this)
        }
        
    @GetMapping
    fun getCurrentCompanySpaces(): ResponseEntity<List<SpaceResponse>> =
        spaceManagementUsecase.getCurrentAdminCompanySpaces().run {
            ResponseEntity.ok(this)
        }
} 