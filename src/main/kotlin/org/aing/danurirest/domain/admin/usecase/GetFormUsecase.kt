package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.FormResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(formId: UUID): FormResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val form =
            formJpaRepository.findByIdAndCompanyId(formId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND)

        return FormResponse.from(form)
    }
}
