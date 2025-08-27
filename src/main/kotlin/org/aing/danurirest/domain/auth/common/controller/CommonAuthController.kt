package org.aing.danurirest.domain.auth.common.controller

import org.aing.danurirest.domain.auth.common.dto.SignInResponse
import org.aing.danurirest.domain.auth.common.usecase.TokenRefreshUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth/common")
class CommonAuthController(
    private val refreshTokenUsecase: TokenRefreshUsecase,
) {
    @GetMapping("refresh")
    fun refreshToken(
        @RequestHeader("Refresh-Token") refreshToken: String,
    ): ResponseEntity<SignInResponse> =
        refreshTokenUsecase.execute(refreshToken).run {
            ResponseEntity.ok(this)
        }
}
