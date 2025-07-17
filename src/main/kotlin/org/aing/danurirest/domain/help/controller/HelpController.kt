package org.aing.danurirest.domain.help.controller

import org.aing.danurirest.domain.help.usecase.CreateHelpUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/help")
class HelpController(
    private val createHelpUsecase: CreateHelpUsecase,
) {
    @GetMapping
    fun help(): ResponseEntity<Unit> =
        createHelpUsecase.execute().run {
            ResponseEntity.noContent().build()
        }
}
