package org.aing.danurirest.global.util

import org.springframework.http.ResponseCookie

object CookieUtil {
    fun createSecureCookie(
        name: String,
        value: String,
        maxAge: Long,
    ): ResponseCookie =
        ResponseCookie
            .from(name, value)
            .path("/")
            .maxAge(maxAge)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .build()

    fun createDeletionCookie(name: String): ResponseCookie =
        ResponseCookie
            .from(name, "")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .build()
}