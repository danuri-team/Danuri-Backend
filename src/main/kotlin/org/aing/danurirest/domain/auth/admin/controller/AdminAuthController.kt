package org.aing.danurirest.domain.auth.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.auth.admin.dto.SignInRequest
import org.aing.danurirest.domain.auth.admin.dto.SignUpAdminRequest
import org.aing.danurirest.domain.auth.admin.usecase.IssuePasswordResetTokenUsecase
import org.aing.danurirest.domain.auth.admin.usecase.SendPasswordResetMessageUsecase
import org.aing.danurirest.domain.auth.admin.usecase.SignInUsecase
import org.aing.danurirest.domain.auth.admin.usecase.SignUpUsecase
import org.aing.danurirest.domain.auth.common.dto.AuthenticationRequest
import org.aing.danurirest.domain.auth.common.dto.AuthorizationCodeRequest
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
    private val sendPasswordResetMessageUsecase: SendPasswordResetMessageUsecase,
    private val issuePasswordResetTokenUsecase: IssuePasswordResetTokenUsecase,
) {
    @PostMapping("sign-in")
    fun signIn(
        @Valid @RequestBody signInRequest: SignInRequest,
    ): ResponseEntity<SignInResponse> =
        signInUsecase.execute(signInRequest).run {
            ResponseEntity.ok(this)
        }

    @PostMapping("sign-up")
    fun signUp(
        @Valid @RequestBody signUpAdminRequest: SignUpAdminRequest,
    ): ResponseEntity<Unit> =
        signUpUsecase.execute(signUpAdminRequest).run {
            ResponseEntity.noContent().build()
        }

    @PostMapping("find-password")
    fun sendVerifyCodeForFindPassword(
        @Valid @RequestBody request: AuthenticationRequest,
    ): ResponseEntity<Unit> =
        sendPasswordResetMessageUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }

    @PostMapping("reset-token")
    fun issueResetTokenForFindPassword(
        @Valid @RequestBody request: AuthorizationCodeRequest,
    ): ResponseEntity<SignInResponse> =
        issuePasswordResetTokenUsecase.execute(request).run {
            ResponseEntity.ok(this)
        }
}
