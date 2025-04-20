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
        val user: ContextDto =
            SecurityContextHolder
                .getContext()
                .authentication.principal
                as ContextDto
        return usageHistoryRepository
            .spaceUsingInfo(
                usageId = usageId,
                userId = user.id!!,
            ).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND)
            }
    }
}
