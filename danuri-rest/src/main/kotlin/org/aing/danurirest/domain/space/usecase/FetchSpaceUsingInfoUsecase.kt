package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FetchSpaceUsingInfoUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(usageId: UUID): UsageHistory {
        val userId = getCurrentUserId()
        return findUsageHistory(usageId, userId)
    }

    private fun getCurrentUserId(): UUID {
        val user = getCurrentUser()
        return user.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
    }

    private fun getCurrentUser(): ContextDto =
        SecurityContextHolder.getContext().authentication.principal as ContextDto

    private fun findUsageHistory(usageId: UUID, userId: UUID): UsageHistory =
        usageHistoryRepository.spaceUsingInfo(usageId, userId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND) }
}
