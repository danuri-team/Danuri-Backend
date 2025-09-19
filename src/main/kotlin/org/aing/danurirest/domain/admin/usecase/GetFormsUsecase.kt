package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.FormResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetFormsUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(): List<FormResponse> {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val forms = formJpaRepository.findAllByCompanyId(adminCompanyId)
        return forms.map { FormResponse.from(it) }
    }
}
