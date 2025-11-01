package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.UsageHistoryCreateRequest
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.admin.dto.UsageHistorySearchRequest
import org.aing.danurirest.domain.admin.dto.UsageHistoryUpdateRequest
import org.aing.danurirest.domain.admin.usecase.CreateUsageHistoryUsecase
import org.aing.danurirest.domain.admin.usecase.ExportUsageHistoryUsecase
import org.aing.danurirest.domain.admin.usecase.GetUsageHistoriesUsecase
import org.aing.danurirest.domain.admin.usecase.GetUsageHistoryUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateUsageHistoryUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/usage")
class AdminUsageController(
    private val createUsageHistoryUsecase: CreateUsageHistoryUsecase,
    private val getUsageHistoryUsecase: GetUsageHistoryUsecase,
    private val getUsageHistoriesUsecase: GetUsageHistoriesUsecase,
    private val updateUsageHistoryUsecase: UpdateUsageHistoryUsecase,
    private val exportUsageHistoryUsecase: ExportUsageHistoryUsecase,
) {
    @PostMapping
    fun createUsageHistory(
        @Valid @RequestBody request: UsageHistoryCreateRequest,
    ): ResponseEntity<Unit> =
        createUsageHistoryUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }

    @PutMapping("/{usageId}")
    fun updateUsageHistory(
        @PathVariable usageId: UUID,
        @Valid @RequestBody request: UsageHistoryUpdateRequest,
    ): ResponseEntity<UsageHistoryResponse> =
        updateUsageHistoryUsecase.execute(usageId, request).run {
            ResponseEntity.ok(this)
        }

    @PostMapping("/search")
    fun searchUsageHistory(
        @Valid @RequestBody request: UsageHistorySearchRequest,
    ): ResponseEntity<List<UsageHistoryResponse>> =
        getUsageHistoriesUsecase.execute(request).run {
            ResponseEntity.ok(this)
        }

    @GetMapping("/{usageId}")
    fun getUsageHistory(
        @PathVariable usageId: UUID,
    ): ResponseEntity<UsageHistoryResponse> =
        getUsageHistoryUsecase.execute(usageId).run {
            ResponseEntity.ok(this)
        }

    @PostMapping("/export")
    fun exportUsageHistory(
        @Valid @RequestBody request: UsageHistorySearchRequest,
    ): ResponseEntity<ByteArray> {
        val excelBytes = exportUsageHistoryUsecase.execute(request)

        return ResponseEntity
            .ok()
            .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Content-Disposition", "attachment; filename=usage_history.xlsx")
            .body(excelBytes)
    }
}
