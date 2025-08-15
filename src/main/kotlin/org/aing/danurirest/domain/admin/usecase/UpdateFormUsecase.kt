package org.aing.danurirest.domain.admin.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danurirest.domain.admin.dto.FormResponse
import org.aing.danurirest.domain.admin.dto.FormUpdateRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UpdateFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
    private val objectMapper: ObjectMapper,
) {
    fun execute(
        formId: UUID,
        request: FormUpdateRequest,
    ): FormResponse {
        validateJsonSchema(request.schema)

        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val form =
            formJpaRepository
                .findById(formId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND) }

        if (form.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        form.title = request.title
        form.schema = request.schema

        val savedForm = formJpaRepository.save(form)
        return FormResponse.from(savedForm)
    }

    private fun validateJsonSchema(schema: String) {
        try {
            objectMapper.readTree(schema)
        } catch (e: Exception) {
            throw CustomException(CustomErrorCode.INVALID_JSON_SCHEMA)
        }
    }
}
