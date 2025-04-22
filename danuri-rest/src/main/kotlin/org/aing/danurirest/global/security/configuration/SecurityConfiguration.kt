@file:Suppress("ktlint:standard:filename")

package org.aing.danurirest.global.security.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danuridomain.persistence.user.enum.Role
import org.aing.danurirest.global.security.filter.JwtFilter
import org.aing.danurirest.global.security.jwt.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration(
    private val jwtProvider: JwtProvider,
) {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        objectMapper: ObjectMapper,
    ): SecurityFilterChain {
        val jwtFilter = JwtFilter(jwtProvider)
        return http
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.GET, "/")
                    .permitAll()
                    // 관리자
                    .requestMatchers(HttpMethod.POST, "/admin/auth/sign-in")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/admin/auth/sign-up")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/admin/auth/refresh")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/admin/auth/info")
                    .hasAuthority(Role.ROLE_ADMIN.name)
                    // 공간 디바이스
                    .requestMatchers(HttpMethod.POST, "/space/**")
                    .hasAuthority(Role.ROLE_DEVICE.name)
                    .requestMatchers(HttpMethod.GET, "/space/**")
                    .hasAuthority(Role.ROLE_DEVICE.name)
                    .requestMatchers(HttpMethod.POST, "/item/**")
                    .hasAuthority(Role.ROLE_DEVICE.name)
                    // 모니터링
                    .requestMatchers(HttpMethod.GET, "/actuator/prometheus")
                    .hasAuthority(Role.ROLE_ADMIN.name)
                    // 기타 요청
                    .anyRequest()
                    .hasAuthority(Role.ROLE_ADMIN.name)
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
            }.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

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
