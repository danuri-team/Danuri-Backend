package org.aing.danurirest.domain.usage.controller

import org.aing.danurirest.domain.common.dto.QrUsageIdRequest
import org.aing.danurirest.domain.usage.usecase.CreateSpaceUsageUsecase
import org.aing.danurirest.domain.usage.usecase.FetchCheckOutUsecase
import org.aing.danurirest.domain.usage.usecase.GetCurrentUsageInfoUsecase
import org.aing.danurirest.persistence.usage.dto.CurrentUsageHistoryDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("usage")
class UsageController(
    private val getCurrentUsageInfoUsecase: GetCurrentUsageInfoUsecase,
    private val fetchCheckOutUsecase: FetchCheckOutUsecase,
    private val createSpaceUsageUsecase: CreateSpaceUsageUsecase,
) {
    @PostMapping
    fun useSpace(
        @RequestParam spaceId: UUID,
    ): ResponseEntity<Unit> =
        createSpaceUsageUsecase.execute(spaceId).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping
    fun getUserCurrentUsage(): ResponseEntity<CurrentUsageHistoryDto> =
        getCurrentUsageInfoUsecase.execute().run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping
    fun checkOutUsingSpace(request: QrUsageIdRequest): ResponseEntity<Unit> =
        fetchCheckOutUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }
}
