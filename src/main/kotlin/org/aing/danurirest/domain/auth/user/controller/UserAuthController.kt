package org.aing.danurirest.domain.auth.user.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.auth.common.dto.AuthenticationRequest
import org.aing.danurirest.domain.auth.common.dto.AuthorizationCodeRequest
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.domain.auth.user.dto.UserRegisterRequest
import org.aing.danurirest.domain.auth.user.usecase.RegisterUserUsecase
import org.aing.danurirest.domain.auth.user.usecase.SendUserAuthCodeUsecase
import org.aing.danurirest.domain.auth.user.usecase.VerifyUserAuthCodeUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/user")
class UserAuthController(
    private val registerUserUsecase: RegisterUserUsecase,
    private val sendUserAuthCodeUsecase: SendUserAuthCodeUsecase,
    private val verifyUserAuthCodeUsecase: VerifyUserAuthCodeUsecase,
) {
    @PostMapping("/register")
    fun registerUser(
        @Valid @RequestBody request: UserRegisterRequest,
    ): ResponseEntity<Unit> =
        registerUserUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }

    @PostMapping("/phone")
    fun sendAuthCode(
        @Valid @RequestBody request: AuthenticationRequest,
    ): ResponseEntity<Unit> =
        sendUserAuthCodeUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }

    @PostMapping("/verify")
    fun verifyAuthCode(
        @Valid @RequestBody request: AuthorizationCodeRequest,
    ): ResponseEntity<SignInResponse> =
        verifyUserAuthCodeUsecase.execute(request.phone, request.authCode).run {
            ResponseEntity.ok(this)
        }
}
