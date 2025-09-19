package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeleteFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(formId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val form =
            formJpaRepository.findByIdAndCompanyId(formId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND)

        formJpaRepository.delete(form)
    }
}
