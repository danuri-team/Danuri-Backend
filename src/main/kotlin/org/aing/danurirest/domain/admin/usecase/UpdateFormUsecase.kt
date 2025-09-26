package org.aing.danurirest.domain.admin.usecase

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
class UpdateFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional
    fun execute(
        formId: UUID,
        request: FormUpdateRequest,
    ): FormResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val form =
            formJpaRepository.findByIdAndCompanyId(formId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND)

        if (request.isSignUpForm && formJpaRepository.existsFormByCompanyIdAndSignUpFormTrue(adminCompanyId) && !form.signUpForm) {
            throw CustomException(CustomErrorCode.FORM_ALREADY_SETUP)
        }

        form.title = request.title
        form.formSchema = request.schema
        form.signUpForm = request.isSignUpForm

        val savedForm = formJpaRepository.save(form)
        return FormResponse.from(savedForm)
    }
}
