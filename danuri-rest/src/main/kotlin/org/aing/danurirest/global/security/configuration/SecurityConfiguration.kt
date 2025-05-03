@file:Suppress("ktlint:standard:filename")

package org.aing.danurirest.global.security.configuration

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
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .cors {
                corsConfig()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.addFilterBefore(JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                // 인증/인가
                it.requestMatchers(HttpMethod.POST, "/admin/auth/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/auth/user/**").permitAll()
                // 헬스체크
                it.requestMatchers(HttpMethod.GET, "/health").permitAll()
                // 디바이스단
                it.requestMatchers(HttpMethod.POST, "/admin/devices/token").permitAll()
                it.requestMatchers(HttpMethod.GET, "/admin/devices/space/").hasRole("DEVICE")
                it.requestMatchers("/admin/**").hasRole("ADMIN")
                it.requestMatchers("/").hasRole("ADMIN")
                it.anyRequest().authenticated()
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
