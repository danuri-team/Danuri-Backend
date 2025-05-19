package org.aing.danurirest.domain.usage.usecase

import org.aing.danuridomain.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.util.GetCurrentUser
import org.springframework.stereotype.Service

@Service
class GetCurrentUsageInfoUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(): CurrentUsageHistoryDto {
        val user = GetCurrentUser.getUser()
        return usageHistoryRepository.findUserCurrentUsageInfo(
            user.id!!,
        )
    }
}
