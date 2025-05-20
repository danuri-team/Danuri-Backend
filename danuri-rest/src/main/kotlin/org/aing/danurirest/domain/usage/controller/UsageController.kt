package org.aing.danurirest.domain.usage.controller

import org.aing.danuridomain.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danurirest.domain.usage.dto.FetchCheckOutRequest
import org.aing.danurirest.domain.usage.usecase.FetchCheckOutUsecase
import org.aing.danurirest.domain.usage.usecase.GetCurrentUsageInfoUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("usage")
class UsageController(
    private val getCurrentUsageInfoUsecase: GetCurrentUsageInfoUsecase,
    private val fetchCheckOutUsecase: FetchCheckOutUsecase,
) {
    @GetMapping
    fun getUserCurrentUsage(): ResponseEntity<CurrentUsageHistoryDto> =
        getCurrentUsageInfoUsecase.execute().run {
            ResponseEntity.ok(this)
        }

    @PostMapping
    fun checkOutUsingSpace(
        @RequestBody fetchCheckOutRequest: FetchCheckOutRequest,
    ): ResponseEntity<Unit> =
        fetchCheckOutUsecase.execute(fetchCheckOutRequest).run {
            ResponseEntity.noContent().build()
        }
}
