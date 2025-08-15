package org.aing.danurirest.domain.admin.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danurirest.domain.admin.dto.FormCreateRequest
import org.aing.danurirest.domain.admin.dto.FormResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.form.entity.Form
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val companyRepository: CompanyJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
    private val objectMapper: ObjectMapper,
) {
    fun execute(request: FormCreateRequest): FormResponse {
        validateJsonSchema(request.schema)

        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val company = companyRepository.findById(adminCompanyId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val form = Form(
            title = request.title,
            schema = request.schema,
            company = company,
        )

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
