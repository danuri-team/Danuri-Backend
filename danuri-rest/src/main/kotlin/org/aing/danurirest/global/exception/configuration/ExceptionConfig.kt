package org.aing.danurirest.global.exception.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danurirest.global.exception.filter.ExceptionFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExceptionConfig {
    @Bean
    fun exceptionFilter(objectMapper: ObjectMapper): FilterRegistrationBean<ExceptionFilter> =
        FilterRegistrationBean<ExceptionFilter>().apply {
            filter = ExceptionFilter(objectMapper)
            order = 1
            urlPatterns = mutableListOf("*")
        }
}