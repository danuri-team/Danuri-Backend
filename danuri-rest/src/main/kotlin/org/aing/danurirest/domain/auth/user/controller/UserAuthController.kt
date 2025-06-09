package org.aing.danurirest.domain.auth.user.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.domain.auth.user.dto.UserAuthCodeResponse
import org.aing.danurirest.domain.auth.user.dto.UserPhoneAuthRequest
import org.aing.danurirest.domain.auth.user.dto.UserRegisterRequest
import org.aing.danurirest.domain.auth.user.dto.UserRegisterResponse
import org.aing.danurirest.domain.auth.user.dto.UserVerifyAuthRequest
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
    ): ResponseEntity<UserRegisterResponse> {
        val user = registerUserUsecase.execute(request)
        return ResponseEntity.ok(UserRegisterResponse.from(user))
    }

    @PostMapping("/phone")
    fun sendAuthCode(
        @Valid @RequestBody request: UserPhoneAuthRequest,
    ): ResponseEntity<UserAuthCodeResponse> {
        sendUserAuthCodeUsecase.execute(request.phone)
        return ResponseEntity.ok(UserAuthCodeResponse(isSuccess = true, message = "인증번호가 발송되었습니다."))
    }

    @PostMapping("/verify")
    fun verifyAuthCode(
        @Valid @RequestBody request: UserVerifyAuthRequest,
    ): ResponseEntity<SignInResponse> =
        verifyUserAuthCodeUsecase.execute(request.phone, request.authCode).run {
            ResponseEntity.ok(this)
        }
}
