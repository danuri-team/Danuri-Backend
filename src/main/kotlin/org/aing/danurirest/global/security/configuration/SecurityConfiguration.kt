package org.aing.danurirest.global.security.configuration

import org.aing.danurirest.global.security.filter.JwtFilter
import org.aing.danurirest.global.security.handler.CustomAuthenticationEntryPoint
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
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .cors {
                it.configurationSource(corsConfig())
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.addFilterBefore(JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthenticationEntryPoint)
            }.authorizeHttpRequests {
                // 인증/인가
                it.requestMatchers(HttpMethod.POST, "/auth/admin/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/auth/common/refresh").permitAll()
                it.requestMatchers(HttpMethod.POST, "/auth/device/token").permitAll()

                // 모니터링
                it.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/health").permitAll()

                // DEVICE
                it.requestMatchers(HttpMethod.POST, "/auth/user/**").hasRole("DEVICE")
                it.requestMatchers(HttpMethod.GET, "/item", "/space").hasRole("DEVICE")
                it.requestMatchers(HttpMethod.GET, "/form").hasRole("DEVICE")
                it.requestMatchers(HttpMethod.POST, "/item").hasRole("DEVICE")
                it.requestMatchers(HttpMethod.DELETE, "/item", "/usage").hasRole("DEVICE")
//                it.requestMatchers("/help/**").hasRole("DEVICE") -> TODO // 운영 계획 다시 논의 필요

                // USER
                it.requestMatchers(HttpMethod.POST, "/usage").hasRole("USER")
                it.requestMatchers(HttpMethod.POST, "/form").hasRole("USER")

                // ADMIN
                it.requestMatchers("/admin/**").hasRole("ADMIN")

                // 그 외
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.anyRequest().permitAll()
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
