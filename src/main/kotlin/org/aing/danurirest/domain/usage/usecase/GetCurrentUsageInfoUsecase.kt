package org.aing.danurirest.domain.usage.usecase

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danurirest.persistence.usage.repository.UsageHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCurrentUsageInfoUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    @Transactional(readOnly = true)
    fun execute(): CurrentUsageHistoryDto {
        val context = PrincipalUtil.getContextDto()
        val userId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
        return usageHistoryRepository.findUserCurrentUsageInfo(
            userId,
        )
    }
}
