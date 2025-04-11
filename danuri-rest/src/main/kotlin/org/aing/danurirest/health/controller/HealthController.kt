package org.aing.danurirest.health.controller

import org.aing.danurirest.health.dto.HealthResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("health")
class HealthController {
    @GetMapping
    fun healthCheck() : ResponseEntity<HealthResponse> = ResponseEntity.ok(HealthResponse())
}