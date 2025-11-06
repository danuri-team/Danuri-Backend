package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.service.ExcelService
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.YearMonth
import java.util.UUID

@Service
class ExportMonthlyUsageExcelUsecase(
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
    private val spaceJpaRepository: SpaceJpaRepository,
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val excelService: ExcelService,
) {
    @Transactional(readOnly = true)
    fun execute(
        spaceId: UUID,
        year: Int,
        month: Int,
    ): ByteArray {
        val companyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceJpaRepository.findById(spaceId).orElseThrow {
                CustomException(CustomErrorCode.NOT_FOUND_SPACE)
            }

        if (space.company.id != companyId) {
            throw CustomException(CustomErrorCode.NOT_FOUND_SPACE)
        }

        val yearMonth = YearMonth.of(year, month)
        val startTime = yearMonth.atDay(1).atStartOfDay()
        val endTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay()

        val usageHistories =
            usageHistoryJpaRepository.findBySpaceAndMonth(
                spaceId = spaceId,
                startTime = startTime,
                endTime = endTime,
            )

        return excelService.createMonthlyUsageExcel(space, yearMonth, usageHistories)
    }
}
