package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.DeleteHelpRequest
import org.aing.danurirest.domain.admin.usecase.DeleteHelpUsecase
import org.aing.danurirest.domain.admin.usecase.GetHelpUsecase
import org.aing.danurirest.persistence.help.entity.HelpHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/helps")
class AdminHelpController(
    private val getHelpUsecase: GetHelpUsecase,
    private val deleteHelpUsecase: DeleteHelpUsecase,
) {
    @GetMapping
    fun getHelp(
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<HelpHistory>> =
        getHelpUsecase.execute(pageable).run {
            ResponseEntity.ok(this)
        }

    @DeleteMapping
    fun deleteHelp(
        @Valid @RequestBody request: DeleteHelpRequest,
    ): ResponseEntity<Unit> =
        deleteHelpUsecase.execute(request).run {
            ResponseEntity.noContent().build()
        }
}
