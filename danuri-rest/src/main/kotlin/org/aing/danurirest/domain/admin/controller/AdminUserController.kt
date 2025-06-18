package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.UserRequest
import org.aing.danurirest.domain.admin.dto.UserResponse
import org.aing.danurirest.domain.admin.usecase.CreateUserUsecase
import org.aing.danurirest.domain.admin.usecase.DeleteUserUsecase
import org.aing.danurirest.domain.admin.usecase.GetUserUsecase
import org.aing.danurirest.domain.admin.usecase.GetUsersUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateUserUsecase
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
    private val createUserUsecase: CreateUserUsecase,
    private val updateUserUsecase: UpdateUserUsecase,
    private val deleteUserUsecase: DeleteUserUsecase,
    private val getUserUsecase: GetUserUsecase,
    private val getCompanyUsersUsecase: GetUsersUsecase,
) {
    @PostMapping
    fun createUser(
        @Valid @RequestBody request: UserRequest,
    ): ResponseEntity<UserResponse> =
        createUserUsecase.execute(request).run {
            ResponseEntity.ok(this)
        }

    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: UserRequest,
    ): ResponseEntity<UserResponse> =
        updateUserUsecase.execute(userId, request).run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: UUID,
    ): ResponseEntity<Unit> =
        deleteUserUsecase.execute(userId).run {
            ResponseEntity.noContent().build()
        }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: UUID,
    ): ResponseEntity<UserResponse> =
        getUserUsecase.execute(userId).run {
            ResponseEntity.ok(this)
        }

    @GetMapping
    fun getCurrentCompanyUsers(): ResponseEntity<List<UserResponse>> =
        getCompanyUsersUsecase.execute().run {
            ResponseEntity.ok(this)
        }
} 
