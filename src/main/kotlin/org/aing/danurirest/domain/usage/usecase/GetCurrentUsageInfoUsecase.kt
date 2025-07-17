package org.aing.danurirest.domain.usage.usecase

import org.aing.danurirest.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danurirest.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.springframework.stereotype.Service

@Service
class GetCurrentUsageInfoUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(): CurrentUsageHistoryDto {
        val context = PrincipalUtil.getContextDto()
        val userId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
        return usageHistoryRepository.findUserCurrentUsageInfo(
            userId,
        )
    }
}
