package org.aing.danurirest.domain.space.controller
import org.aing.danurirest.domain.space.dto.GetSpaceStatusByDeviceIdResponse
import org.aing.danurirest.domain.space.dto.UseSpaceRequest
import org.aing.danurirest.domain.space.usecase.CreateSpaceUsageUsecase
import org.aing.danurirest.domain.space.usecase.GetSpaceStatusUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/space")
class SpaceController(
    private val createSpaceUsageUsecase: CreateSpaceUsageUsecase,
    private val getSpaceStatusUsecase: GetSpaceStatusUsecase,
) {
    @PostMapping
    fun useSpace(
        @RequestBody useSpaceRequest: UseSpaceRequest,
    ): ResponseEntity<Unit> =
        createSpaceUsageUsecase.execute(useSpaceRequest).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping
    fun getSpaceStatus(): ResponseEntity<List<GetSpaceStatusByDeviceIdResponse>> =
        getSpaceStatusUsecase.execute().run {
            ResponseEntity.ok(this)
        }
}
