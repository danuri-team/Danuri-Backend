package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.UsageHistoryCreateRequest
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.admin.dto.UsageHistorySearchRequest
import org.aing.danurirest.domain.admin.dto.UsageHistoryUpdateRequest
import org.aing.danurirest.domain.admin.usecase.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/admin/usage")
class AdminUsageController(
    private val createUsageHistoryUsecase: CreateUsageHistoryUsecase,
    private val getUsageHistoryUsecase: GetUsageHistoryUsecase,
    private val getUsageHistoriesUsecase: GetUsageHistoriesUsecase,
    private val updateUsageHistoryUsecase: UpdateUsageHistoryUsecase,
    private val exportUsageHistoryUsecase: ExportUsageHistoryUsecase,
    private val exportMonthlyUsageExcelUsecase: ExportMonthlyUsageExcelUsecase,
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
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<UsageHistoryResponse>> =
        getUsageHistoriesUsecase.execute(request, pageable).run {
            ResponseEntity.ok(this)
        }

    @GetMapping("/{usageId}")
    fun getUsageHistory(
        @PathVariable usageId: UUID,
    ): ResponseEntity<UsageHistoryResponse> =
        getUsageHistoryUsecase.execute(usageId).run {
            ResponseEntity.ok(this)
        }

    @PostMapping("/range-usage-excel")
    fun exportUsageHistory(
        @Valid @RequestBody request: UsageHistorySearchRequest,
    ): ResponseEntity<ByteArray> {
        val excelBytes = exportUsageHistoryUsecase.execute(request)

        return ResponseEntity
            .ok()
            .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Content-Disposition", "attachment; filename=usage_history_.xlsx")
            .body(excelBytes)
    }

    @GetMapping("/{spaceId}/monthly-usage-excel")
    fun exportMonthlyUsageExcel(
        @PathVariable spaceId: UUID,
        @RequestParam year: Int,
        @RequestParam month: Int,
    ): ResponseEntity<ByteArray> {
        val excelBytes = exportMonthlyUsageExcelUsecase.execute(spaceId, year, month)

        return ResponseEntity
            .ok()
            .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Content-Disposition", "attachment; filename=monthly_usage_${year}_$month.xlsx")
            .body(excelBytes)
    }
}
