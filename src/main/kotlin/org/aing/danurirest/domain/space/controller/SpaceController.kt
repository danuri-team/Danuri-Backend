package org.aing.danurirest.domain.space.controller
import org.aing.danurirest.domain.space.dto.GetSpaceStatusByDeviceIdResponse
import org.aing.danurirest.domain.space.usecase.GetSpaceStatusUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/space")
class SpaceController(
    private val getSpaceStatusUsecase: GetSpaceStatusUsecase,
) {
    @GetMapping
    fun getSpaceStatus(): ResponseEntity<List<GetSpaceStatusByDeviceIdResponse>> =
        getSpaceStatusUsecase.execute().run {
            ResponseEntity.ok(this)
        }
}
