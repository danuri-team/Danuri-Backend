package org.aing.danuri.global.exception.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aing.danuri.global.exception.CustomException
import org.aing.danuri.global.exception.dto.CustomExceptionResponse
import org.aing.danuri.global.exception.enums.CustomErrorCode
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter

class ExceptionFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (exception: Exception) {
            val exceptionResponse =
                if (exception is CustomException) {
                    CustomExceptionResponse(exception.customErrorCode)
                } else {
                    CustomExceptionResponse(CustomErrorCode.UNKNOWN_SERVER_ERROR)
                }
            response.status = exceptionResponse.status.statusCode.value()
            response.characterEncoding = Charsets.UTF_8.name()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.print(objectMapper.writeValueAsString(exceptionResponse))
        }
    }
}