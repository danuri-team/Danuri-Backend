package org.aing.danurirest.domain.admin.controller

import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.admin.dto.UsageHistorySearchRequest
import org.aing.danurirest.domain.admin.service.ExcelService
import org.aing.danurirest.domain.admin.usecase.GetUsageHistoryUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/usage")
class AdminUsageController(
    private val getUsageHistoryUsecase: GetUsageHistoryUsecase,
    private val excelService: ExcelService
) {
    @PostMapping("/search")
    fun searchUsageHistory(
        @RequestBody request: UsageHistorySearchRequest
    ): ResponseEntity<List<UsageHistoryResponse>> =
        getUsageHistoryUsecase.execute(request).run {
            ResponseEntity.ok(this)
        }
    
    @GetMapping("/{usageId}")
    fun getUsageHistory(
        @PathVariable usageId: UUID
    ): ResponseEntity<UsageHistoryResponse> =
        getUsageHistoryUsecase.executeById(usageId).run {
            ResponseEntity.ok(this)
        }
    
    @PostMapping("/export")
    fun exportUsageHistory(
        @RequestBody request: UsageHistorySearchRequest
    ): ResponseEntity<ByteArray> {
        val histories = getUsageHistoryUsecase.execute(request)
        val excelBytes = excelService.createUsageHistoryExcel(histories)
        
        return ResponseEntity.ok()
            .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Content-Disposition", "attachment; filename=usage_history.xlsx")
            .body(excelBytes)
    }
} 