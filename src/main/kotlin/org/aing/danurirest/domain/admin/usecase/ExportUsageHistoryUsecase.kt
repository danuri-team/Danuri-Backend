package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UsageHistorySearchRequest
import org.aing.danurirest.domain.admin.service.ExcelService
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExportUsageHistoryUsecase(
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
    private val getUsageHistoriesUsecase: GetUsageHistoriesUsecase,
    private val formJpaRepository: FormJpaRepository,
    private val excelService: ExcelService,
) {
    @Transactional(readOnly = true)
    fun execute(request: UsageHistorySearchRequest): ByteArray {
        val companyId = getAdminCompanyIdUsecase.execute()
        val histories = getUsageHistoriesUsecase.execute(request, Pageable.unpaged()).content
        val signUpForm =
            formJpaRepository.findByCompanyIdAndSignUpFormTrue(companyId).orElseThrow {
                CustomException(
                    CustomErrorCode.FORM_IS_NOT_SETUP,
                )
            }

        return excelService.createUsageHistoryExcel(histories, signUpForm)
    }
}
