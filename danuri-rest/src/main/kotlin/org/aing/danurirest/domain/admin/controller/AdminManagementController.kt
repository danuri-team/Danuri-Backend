package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.AdminPasswordUpdateRequest
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.admin.dto.AdminUpdateRequest
import org.aing.danurirest.domain.admin.usecase.AdminManagementUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/management")
class AdminManagementController(
    private val adminManagementUsecase: AdminManagementUsecase
) {
    @GetMapping("/me")
    fun getMyInfo(): ResponseEntity<AdminResponse> =
        adminManagementUsecase.getCurrentAdminInfo().run {
            ResponseEntity.ok(this)
        }
    
    @GetMapping("/{adminId}")
    fun getAdminInfo(@PathVariable adminId: UUID): ResponseEntity<AdminResponse> =
        adminManagementUsecase.getAdminInfo(adminId).run {
            ResponseEntity.ok(this)
        }
    
    @GetMapping("/company/{companyId}")
    fun getAdminsByCompany(@PathVariable companyId: UUID): ResponseEntity<List<AdminResponse>> =
        adminManagementUsecase.getAdminsByCompany(companyId).run {
            ResponseEntity.ok(this)
        }
    
    @PutMapping("/{adminId}")
    fun updateAdmin(
        @PathVariable adminId: UUID,
        @Valid @RequestBody request: AdminUpdateRequest
    ): ResponseEntity<AdminResponse> =
        adminManagementUsecase.updateAdmin(adminId, request).run {
            ResponseEntity.ok(this)
        }
    
    @PutMapping("/password")
    fun updatePassword(
        @Valid @RequestBody request: AdminPasswordUpdateRequest
    ): ResponseEntity<AdminResponse> =
        adminManagementUsecase.updatePassword(request).run {
            ResponseEntity.ok(this)
        }
    
    @DeleteMapping("/{adminId}")
    fun deleteAdmin(@PathVariable adminId: UUID): ResponseEntity<Unit> =
        adminManagementUsecase.deleteAdmin(adminId).run {
            ResponseEntity.noContent().build()
        }
} 