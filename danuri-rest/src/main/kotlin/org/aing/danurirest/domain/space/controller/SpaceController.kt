package org.aing.danurirest.domain.space.controller

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danurirest.domain.space.usecase.GetAvailableSpaceUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/space")
class SpaceController(
    private val getAvailableSpaceUsecase: GetAvailableSpaceUsecase,
) {
    @GetMapping("{spaceId}")
    fun getAvailableSpaceByCompanyId(
        @PathVariable("spaceId") spaceId: UUID,
    ): ResponseEntity<List<Space>> =
        getAvailableSpaceUsecase.execute(spaceId).run {
            ResponseEntity.ok(this)
        }
}
