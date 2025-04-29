package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.domain.admin.dto.UsageHistoryResponse
import org.aing.danurirest.domain.admin.dto.UsageHistorySearchRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetUsageHistoryUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(request: UsageHistorySearchRequest): List<UsageHistoryResponse> {
        val user: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        
        val histories = when {
            request.spaceId != null -> {
                usageHistoryRepository.findAllBySpaceIdAndDateRange(
                    request.spaceId,
                    request.startDate,
                    request.endDate
                )
            }
            request.userId != null -> {
                usageHistoryRepository.findAllByUserIdAndDateRange(
                    request.userId,
                    request.startDate,
                    request.endDate
                )
            }
            request.companyId != null -> {
                usageHistoryRepository.findAllByCompanyIdAndDateRange(
                    request.companyId,
                    request.startDate,
                    request.endDate
                )
            }
            else -> {
                throw CustomException(CustomErrorCode.VALIDATION_ERROR)
            }
        }
        
        return histories.map { UsageHistoryResponse.from(it) }
    }
    
    fun executeById(usageId: UUID): UsageHistoryResponse {
        val usage = usageHistoryRepository.findById(usageId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        
        return UsageHistoryResponse.from(usage)
    }
} 