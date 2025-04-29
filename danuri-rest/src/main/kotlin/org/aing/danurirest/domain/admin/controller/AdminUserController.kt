package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.UserRequest
import org.aing.danurirest.domain.admin.dto.UserResponse
import org.aing.danurirest.domain.admin.usecase.UserManagementUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/users")
class AdminUserController(
    private val userManagementUsecase: UserManagementUsecase
) {
    @PostMapping
    fun createUser(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> =
        userManagementUsecase.createUser(request).run {
            ResponseEntity.ok(this)
        }
    
    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: UserRequest
    ): ResponseEntity<UserResponse> =
        userManagementUsecase.updateUser(userId, request).run {
            ResponseEntity.ok(this)
        }
    
    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: UUID): ResponseEntity<Unit> =
        userManagementUsecase.deleteUser(userId).run {
            ResponseEntity.noContent().build()
        }
    
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID): ResponseEntity<UserResponse> =
        userManagementUsecase.getUser(userId).run {
            ResponseEntity.ok(this)
        }
    
    @GetMapping("/company/{companyId}")
    fun getUsersByCompany(@PathVariable companyId: UUID): ResponseEntity<List<UserResponse>> =
        userManagementUsecase.getUsersByCompany(companyId).run {
            ResponseEntity.ok(this)
        }
        
    @GetMapping("/current-company")
    fun getCurrentCompanyUsers(): ResponseEntity<List<UserResponse>> =
        userManagementUsecase.getCurrentAdminCompanyUsers().run {
            ResponseEntity.ok(this)
        }
} 