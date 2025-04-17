@file:Suppress("ktlint:standard:filename")

package org.aing.danurirest.global.security.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        objectMapper: ObjectMapper,
    ): SecurityFilterChain =
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.GET, "/")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/refresh")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/actuator/prometheus")
//                    .hasAuthority(Role.ROLE_ADMIN.name)
//                    .requestMatchers("/admin/**")
//                    .hasAuthority(Role.ROLE_ADMIN.name)
//                    .anyRequest()
//                    .hasAuthority(Role.ROLE_USER.name)
            }.csrf {
                it.disable()
            }.formLogin {
                it.disable()
            }.httpBasic {
                it.disable()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.cors {
                it.configurationSource(corsConfig())
//            }.exceptionHandling {
//                it
//                    .authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper))
//                    .accessDeniedHandler(CustomAccessDeniedHandler(objectMapper))
            }.build()

    fun corsConfig(): CorsConfigurationSource {
        val corsConfigurationSource = CorsConfiguration()
        corsConfigurationSource.addAllowedHeader("*")
        corsConfigurationSource.addAllowedMethod("*")
        corsConfigurationSource.addAllowedOriginPattern("*")
        corsConfigurationSource.addExposedHeader("*")
        corsConfigurationSource.allowCredentials = true

        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfigurationSource)
        return urlBasedCorsConfigurationSource
    }
}
