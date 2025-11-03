package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.AdminPasswordUpdateRequest
import org.aing.danurirest.domain.admin.dto.AdminResponse
import org.aing.danurirest.domain.admin.dto.AdminUpdateRequest
import org.aing.danurirest.domain.admin.usecase.GetAdminUsecase
import org.aing.danurirest.domain.admin.usecase.GetAdminsUsecase
import org.aing.danurirest.domain.admin.usecase.GetCurrentAdminUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateAdminPasswordUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateAdminUsecase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
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
    private val getCurrentAdminUsecase: GetCurrentAdminUsecase,
    private val getAdminUsecase: GetAdminUsecase,
    private val getCompanyAdminsUsecase: GetAdminsUsecase,
    private val updateAdminUsecase: UpdateAdminUsecase,
    private val updateAdminPasswordUsecase: UpdateAdminPasswordUsecase,
) {
    @GetMapping("/me")
    fun getMyInfo(): ResponseEntity<AdminResponse> =
        getCurrentAdminUsecase.execute().run {
            ResponseEntity.ok(this)
        }

    @GetMapping("/{adminId}")
    fun getAdminInfo(
        @PathVariable adminId: UUID,
    ): ResponseEntity<AdminResponse> =
        getAdminUsecase.execute(adminId).run {
            ResponseEntity.ok(this)
        }

    @GetMapping
    fun getAdminsByCompany(
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<AdminResponse>> =
        getCompanyAdminsUsecase.execute(pageable).run {
            ResponseEntity.ok(this)
        }

    @PutMapping
    fun updateAdmin(
        @Valid @RequestBody request: AdminUpdateRequest,
    ): ResponseEntity<Unit> =
        updateAdminUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }

    @PutMapping("/change-password")
    fun updatePassword(
        @Valid @RequestBody request: AdminPasswordUpdateRequest,
    ): ResponseEntity<Unit> =
        updateAdminPasswordUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }
}
