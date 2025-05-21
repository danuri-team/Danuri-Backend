package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.admin.dto.UsageHistorySearchRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetUsageHistoryUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: UsageHistorySearchRequest): List<UsageHistoryResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()

        val histories =
            when {
                request.spaceId != null -> {
                    usageHistoryRepository.findAllByCompanyIdAndSpaceIdAndDateRange(
                        request.spaceId,
                        request.startDate,
                        request.endDate,
                        companyId,
                    )
                }
                request.userId != null -> {
                    usageHistoryRepository.findAllByUserIdAndDateRangeAndCompanyId(
                        request.userId,
                        request.startDate,
                        request.endDate,
                        companyId,
                    )
                }
                else -> {
                    usageHistoryRepository.findAllByCompanyIdAndDateRange(
                        companyId,
                        request.startDate,
                        request.endDate,
                    )
                }
            }

        if (histories != null) {
            return histories.map { UsageHistoryResponse.from(it) }
        } else {
            throw CustomException(CustomErrorCode.NOT_FOUND)
        }
    }

    fun executeById(usageId: UUID): UsageHistoryResponse {
        val companyId = getAdminCompanyIdUsecase.execute()
        val usage =
            usageHistoryRepository
                .findByIdAndCompanyId(usageId, companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        return UsageHistoryResponse.from(usage)
    }
}
