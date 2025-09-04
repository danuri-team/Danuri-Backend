package org.aing.danurirest.global.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aing.danurirest.global.exception.dto.CustomExceptionResponse
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        val exceptionResponse = CustomExceptionResponse(CustomErrorCode.MISSING_TOKEN)

        response.status = exceptionResponse.status.status.value()
        response.characterEncoding = Charsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store")
        response.writer.print(objectMapper.writeValueAsString(exceptionResponse))
        response.writer.flush()
    }
}
