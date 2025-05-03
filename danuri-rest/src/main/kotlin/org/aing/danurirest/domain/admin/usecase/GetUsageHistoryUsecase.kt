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
        // TODO: 조회자 회사에 기반해서만 쿼리가 가능하도록 수정 해야함
        
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
            else -> {
                throw CustomException(CustomErrorCode.VALIDATION_ERROR)
            }
        }
        
        return histories.map { UsageHistoryResponse.from(it) }
    }
    
    fun executeById(usageId: UUID): UsageHistoryResponse {
        val usage = usageHistoryRepository.findById(usageId)
            .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_USER) }
        // TODO: 조회자 회사에 기반해서만 쿼리가 가능하도록 수정 해야함

        return UsageHistoryResponse.from(usage)
    }
} 