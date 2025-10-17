package org.aing.danurirest.domain.auth.common.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
        @RequestHeader("Refresh-Token") refreshToken: String?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<SignInResponse> =
        refreshTokenUsecase.execute(refreshToken, request, response).run {
            ResponseEntity.ok(this)
        }
}
