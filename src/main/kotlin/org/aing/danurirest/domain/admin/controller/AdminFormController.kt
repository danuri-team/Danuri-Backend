package org.aing.danurirest.domain.admin.controller

import jakarta.validation.Valid
import org.aing.danurirest.domain.admin.dto.FormCreateRequest
import org.aing.danurirest.domain.admin.dto.FormResponse
import org.aing.danurirest.domain.admin.dto.FormUpdateRequest
import org.aing.danurirest.domain.admin.usecase.CreateFormUsecase
import org.aing.danurirest.domain.admin.usecase.DeleteFormUsecase
import org.aing.danurirest.domain.admin.usecase.GetFormUsecase
import org.aing.danurirest.domain.admin.usecase.GetFormsUsecase
import org.aing.danurirest.domain.admin.usecase.UpdateFormUsecase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/admin/forms")
class AdminFormController(
    private val createFormUsecase: CreateFormUsecase,
    private val getFormUsecase: GetFormUsecase,
    private val getFormsUsecase: GetFormsUsecase,
    private val updateFormUsecase: UpdateFormUsecase,
    private val deleteFormUsecase: DeleteFormUsecase,
) {
    @PostMapping
    fun createForm(
        @Valid @RequestBody request: FormCreateRequest,
    ): ResponseEntity<FormResponse> {
        val formResponse = createFormUsecase.execute(request)
        return ResponseEntity.ok(formResponse)
    }

    @GetMapping("/{formId}")
    fun getForm(
        @PathVariable formId: UUID,
    ): ResponseEntity<FormResponse> {
        val formResponse = getFormUsecase.execute(formId)
        return ResponseEntity.ok(formResponse)
    }

    @GetMapping
    fun getForms(): ResponseEntity<List<FormResponse>> {
        val formResponses = getFormsUsecase.execute()
        return ResponseEntity.ok(formResponses)
    }

    @PutMapping("/{formId}")
    fun updateForm(
        @PathVariable formId: UUID,
        @Valid @RequestBody request: FormUpdateRequest,
    ): ResponseEntity<FormResponse> {
        val formResponse = updateFormUsecase.execute(formId, request)
        return ResponseEntity.ok(formResponse)
    }

    @DeleteMapping("/{formId}")
    fun deleteForm(
        @PathVariable formId: UUID,
    ): ResponseEntity<Unit> {
        deleteFormUsecase.execute(formId)
        return ResponseEntity.noContent().build()
    }
}
