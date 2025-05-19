package org.aing.danurirest.domain.usage.controller

import org.aing.danuridomain.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danurirest.domain.usage.usecase.GetCurrentUsageInfoUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("usage")
class UsageController(
    private val getCurrentUsageInfoUsecase: GetCurrentUsageInfoUsecase,
) {
    @GetMapping
    fun getUserCurrentUsage(): ResponseEntity<CurrentUsageHistoryDto> =
        getCurrentUsageInfoUsecase.execute().run {
            ResponseEntity.ok(this)
        }
}
