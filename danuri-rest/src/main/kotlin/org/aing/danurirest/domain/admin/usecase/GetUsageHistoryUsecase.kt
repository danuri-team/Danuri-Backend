package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class GetUsageHistoryUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(usageId: UUID): UsageHistoryResponse {
        val companyId = getAdminCompanyIdUsecase.execute()
        val usage =
            usageHistoryRepository
                .findByIdAndCompanyId(usageId, companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        return UsageHistoryResponse.from(usage)
    }
}
