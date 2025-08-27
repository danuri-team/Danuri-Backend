package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class DeleteFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(formId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val form =
            formJpaRepository
                .findById(formId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND) }

        if (form.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        formJpaRepository.delete(form)
    }
}
