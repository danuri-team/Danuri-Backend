package org.aing.danurirest.domain.space.controller

import org.aing.danurirest.domain.space.dto.IsUsingSpaceResponse
import org.aing.danurirest.domain.space.dto.SpaceUsageResponse
import org.aing.danurirest.domain.space.dto.SpaceUsingInfoResponse
import org.aing.danurirest.domain.space.usecase.CreateSpaceUsageUsecase
import org.aing.danurirest.domain.space.usecase.FetchSpaceUsingInfoUsecase
import org.aing.danurirest.domain.space.usecase.FetchSpaceUsingTimeUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/space")
class SpaceController(
    private val getSpaceUsingTimeUsecase: FetchSpaceUsingTimeUsecase,
    private val getSpaceUsingInfoUsecase: FetchSpaceUsingInfoUsecase,
    private val createSpaceUsageUsecase: CreateSpaceUsageUsecase,
) {
    @GetMapping("{spaceId}")
    fun getSpaceUsingTime(
        @PathVariable("spaceId") spaceId: UUID,
    ): ResponseEntity<List<SpaceUsageResponse>> =
        getSpaceUsingTimeUsecase.execute(spaceId).run {
            ResponseEntity.ok(this.map { SpaceUsageResponse.from(it) })
        }

    @GetMapping("in-use/{usageId}")
    fun getSpaceUsingInfo(
        @PathVariable("usageId") usageId: UUID,
    ): ResponseEntity<SpaceUsingInfoResponse> =
        getSpaceUsingInfoUsecase.execute(usageId).run {
            ResponseEntity.ok(SpaceUsingInfoResponse.from(this))
        }

    @PostMapping("{spaceId}")
    fun useSpace(
        @PathVariable("spaceId") spaceId: UUID,
    ): ResponseEntity<IsUsingSpaceResponse> =
        createSpaceUsageUsecase.execute(spaceId).run {
            ResponseEntity.ok(IsUsingSpaceResponse(true))
        }
}
