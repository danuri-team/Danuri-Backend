package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.SpaceRequest
import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.admin.usecase.CreateSpaceUsecase
import org.aing.danurirest.domain.admin.usecase.DeleteSpaceUsecase
import org.aing.danurirest.domain.admin.usecase.GetSpaceUsecase
import org.aing.danurirest.domain.admin.usecase.GetSpacesUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateSpaceUsecase
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
    private val createSpaceUsecase: CreateSpaceUsecase,
    private val updateSpaceUsecase: UpdateSpaceUsecase,
    private val deleteSpaceUsecase: DeleteSpaceUsecase,
    private val getSpaceUsecase: GetSpaceUsecase,
    private val getCompanySpacesUsecase: GetSpacesUsecase,
) {
    @PostMapping
    fun createSpace(
        @Valid @RequestBody request: SpaceRequest,
    ): ResponseEntity<SpaceResponse> =
        createSpaceUsecase.execute(request).run {
            ResponseEntity.ok(this)
        }

    @PutMapping("/{spaceId}")
    fun updateSpace(
        @PathVariable spaceId: UUID,
        @Valid @RequestBody request: SpaceRequest,
    ): ResponseEntity<SpaceResponse> =
        updateSpaceUsecase.execute(spaceId, request).run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping("/{spaceId}")
    fun deleteSpace(
        @PathVariable spaceId: UUID,
    ): ResponseEntity<Unit> =
        deleteSpaceUsecase.execute(spaceId).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping("/{spaceId}")
    fun getSpace(
        @PathVariable spaceId: UUID,
    ): ResponseEntity<SpaceResponse> =
        getSpaceUsecase.execute(spaceId).run {
            ResponseEntity.ok(this)
        }

    @GetMapping
    fun getCurrentCompanySpaces(): ResponseEntity<List<SpaceResponse>> =
        getCompanySpacesUsecase.execute().run {
            ResponseEntity.ok(this)
        }
}
