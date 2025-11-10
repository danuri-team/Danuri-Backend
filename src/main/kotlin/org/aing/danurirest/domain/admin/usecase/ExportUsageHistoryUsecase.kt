package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.admin.dto.UsageHistorySearchRequest
import org.aing.danurirest.domain.admin.service.excel.ExcelService
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.data.domain.PageRequest
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

        val allHistories = mutableListOf<UsageHistoryResponse>()
        var pageable: Pageable = PageRequest.of(0, 100)

        while (true) {
            val page = getUsageHistoriesUsecase.execute(request, pageable)
            allHistories.addAll(page.content)
            if (!page.hasNext()) {
                break
            }
            pageable = page.nextPageable()
        }

        val signUpForm =
            formJpaRepository.findByCompanyIdAndSignUpFormTrue(companyId).orElseThrow {
                CustomException(
                    CustomErrorCode.FORM_IS_NOT_SETUP,
                )
            }

        return excelService.createUsageHistoryExcel(allHistories, signUpForm)
    }
}
