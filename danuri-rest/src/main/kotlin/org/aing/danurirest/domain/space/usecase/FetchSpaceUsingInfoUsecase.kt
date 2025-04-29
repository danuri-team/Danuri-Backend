package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FetchSpaceUsingInfoUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(): UsageHistory {
        val context = getCurrentContext()
        val userId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        val now = LocalDateTime.now()
        val usageHistories =
            usageHistoryRepository.findAllByUserIdAndDateRange(
                userId = userId,
                startDate = now.minusHours(1),
                endDate = now.plusHours(1),
            ) // 수정필요

        return usageHistories.firstOrNull {
            !it.start_at.isAfter(now) && (it.end_at != null && !it.end_at!!.isAfter(now)) // 수정필요
        } ?: throw CustomException(CustomErrorCode.NOT_FOUND)
    }

    private fun getCurrentContext(): ContextDto {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as? ContextDto
            ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
    }
}
