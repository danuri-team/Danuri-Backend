package org.aing.danurirest.domain.auth.admin.controller

import org.aing.danurirest.domain.auth.admin.dto.AdminInfoResponse
import org.aing.danurirest.domain.auth.admin.dto.DeviceSignInRequest
import org.aing.danurirest.domain.auth.admin.dto.RegisterDeviceRequest
import org.aing.danurirest.domain.auth.admin.dto.SignInRequest
import org.aing.danurirest.domain.auth.admin.dto.SignInResponse
import org.aing.danurirest.domain.auth.admin.dto.SignUpAdminRequest
import org.aing.danurirest.domain.auth.admin.dto.TokenRefreshRequest
import org.aing.danurirest.domain.auth.admin.usecase.DeviceSignInUsecase
import org.aing.danurirest.domain.auth.admin.usecase.FetchAdminInfoUsecase
import org.aing.danurirest.domain.auth.admin.usecase.RegisterDeviceUsecase
import org.aing.danurirest.domain.auth.admin.usecase.SignInUsecase
import org.aing.danurirest.domain.auth.admin.usecase.SignUpUsecase
import org.aing.danurirest.domain.auth.admin.usecase.TokenRefreshUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin/auth")
class AuthController(
    private val signInUsecase: SignInUsecase,
    private val refreshTokenUsecase: TokenRefreshUsecase,
    private val fetchAdminInfoUsecase: FetchAdminInfoUsecase,
    private val signUpUsecase: SignUpUsecase,
    private val registerDeviceUsecase: RegisterDeviceUsecase,
    private val deviceSignInUsecase: DeviceSignInUsecase,
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

    @PostMapping("refresh")
    fun refreshToken(
        @RequestBody refreshRequest: TokenRefreshRequest,
    ): ResponseEntity<SignInResponse> =
        refreshTokenUsecase.execute(refreshRequest).run {
            ResponseEntity.ok(this)
        }

    @GetMapping("info")
    fun getAdminInfo(): ResponseEntity<AdminInfoResponse> = fetchAdminInfoUsecase.execute().run { ResponseEntity.ok(this) }

    @PostMapping("register-device")
    fun registerDevice(
        @RequestBody registerDeviceRequest: RegisterDeviceRequest,
    ): ResponseEntity<Unit> =
        registerDeviceUsecase.execute(registerDeviceRequest).run {
            ResponseEntity.noContent().build()
        }

    @PostMapping("use-device")
    fun useDevice(
        @RequestBody deviceRegisterRequest: DeviceSignInRequest,
    ): ResponseEntity<SignInResponse> =
        deviceSignInUsecase.execute(deviceRegisterRequest).run {
            ResponseEntity.ok(this)
        }
}
