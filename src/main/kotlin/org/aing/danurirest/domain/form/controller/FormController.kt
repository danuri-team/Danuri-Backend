package org.aing.danurirest.domain.form.controller

import org.aing.danurirest.domain.form.dto.CreateSignUpFormResultRequest
import org.aing.danurirest.domain.form.usecase.CreateSignUpFormResultUsecase
import org.aing.danurirest.domain.form.usecase.GetSignUpFormUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/form")
class FormController(
    private val createSignUpFormResultUsecase: CreateSignUpFormResultUsecase,
    private val getSignUpFormUsecase: GetSignUpFormUsecase,
) {
    @GetMapping
    fun getSignUpForm() {
        getSignUpFormUsecase.execute().run {
            ResponseEntity.ok(this)
        }
    }

    @PostMapping
    fun createSignUpFormResult(
        @RequestBody request: CreateSignUpFormResultRequest,
    ): ResponseEntity<Unit> =
        createSignUpFormResultUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }
}
