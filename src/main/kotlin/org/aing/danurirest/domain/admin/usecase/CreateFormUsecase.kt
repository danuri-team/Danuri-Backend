package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.FormCreateRequest
import org.aing.danurirest.domain.admin.dto.FormResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.form.entity.Form
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val companyRepository: CompanyJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: FormCreateRequest): FormResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val company =
            companyRepository
                .findById(adminCompanyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        if (request.isSignUpForm && formJpaRepository.existsFormByCompanyIdAndSignUpFormTrue(company.id!!)) {
            throw CustomException(CustomErrorCode.FORM_ALREADY_SETUP)
        }

        val form =
            Form(
                title = request.title,
                formSchema = request.schema,
                company = company,
                signUpForm = request.isSignUpForm,
            )

        val savedForm = formJpaRepository.save(form)
        return FormResponse.from(savedForm)
    }
}
