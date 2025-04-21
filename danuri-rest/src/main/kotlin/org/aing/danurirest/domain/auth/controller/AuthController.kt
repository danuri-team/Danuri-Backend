package org.aing.danurirest.domain.auth.controller

import org.aing.danurirest.domain.auth.dto.AdminInfoResponse
import org.aing.danurirest.domain.auth.dto.SignInRequest
import org.aing.danurirest.domain.auth.dto.SignInResponse
import org.aing.danurirest.domain.auth.dto.SignUpAdminRequest
import org.aing.danurirest.domain.auth.dto.TokenRefreshRequest
import org.aing.danurirest.domain.auth.usecase.FetchAdminInfoUsecase
import org.aing.danurirest.domain.auth.usecase.SignInUsecase
import org.aing.danurirest.domain.auth.usecase.SignUpUsecase
import org.aing.danurirest.domain.auth.usecase.TokenRefreshUsecase
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
) {
    @PostMapping("sign-in")
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
    ): ResponseEntity<SignInResponse> =
        signInUsecase.execute(signInRequest).run {
            ResponseEntity.ok(this)
        }

    @PostMapping("sign-up")
    fun signIp(
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
}
