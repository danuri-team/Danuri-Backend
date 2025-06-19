package org.aing.danurirest.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOriginPatterns("https://*.danuri-admin-frontend.pages.dev", "http://localhost:3000")
            .allowedMethods("*")
            .allowCredentials(true)
    }
}
