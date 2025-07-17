package org.aing.danurirest.domain.auth.admin.controller

import org.aing.danurirest.domain.auth.admin.dto.SignInRequest
import org.aing.danurirest.domain.auth.admin.dto.SignUpAdminRequest
import org.aing.danurirest.domain.auth.admin.usecase.SignInUsecase
import org.aing.danurirest.domain.auth.admin.usecase.SignUpUsecase
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth/admin")
class AdminAuthController(
    private val signInUsecase: SignInUsecase,
    private val signUpUsecase: SignUpUsecase,
) {
    @PostMapping("sign-in")
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
    ): ResponseEntity<SignInResponse> =
        signInUsecase.execute(signInRequest).run {
            ResponseEntity.ok(this)
        }

    @PostMapping("sign-up")
    fun signUp(
        @RequestBody signUpAdminRequest: SignUpAdminRequest,
    ): ResponseEntity<Unit> =
        signUpUsecase.execute(signUpAdminRequest).run {
            ResponseEntity.noContent().build()
        }
}
