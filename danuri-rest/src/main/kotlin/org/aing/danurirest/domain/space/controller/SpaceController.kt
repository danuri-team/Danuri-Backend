package org.aing.danurirest.domain.space.controller

import org.aing.danurirest.domain.space.dto.SpaceUsageResponse
import org.aing.danurirest.domain.space.usecase.GetSpaceUsingTimeUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/space")
class SpaceController(
    private val getSpaceUsingTimeUsecase: GetSpaceUsingTimeUsecase,
) {
    @GetMapping("{spaceId}")
    fun getSpaceUsingTime(
        @PathVariable("spaceId") spaceId: UUID,
    ): ResponseEntity<List<SpaceUsageResponse>> =
        getSpaceUsingTimeUsecase.execute(spaceId).run {
            ResponseEntity.ok(this.map { SpaceUsageResponse.from(it) })
        }
}
