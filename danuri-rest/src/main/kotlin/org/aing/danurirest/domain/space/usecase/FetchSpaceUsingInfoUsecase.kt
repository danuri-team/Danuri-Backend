package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FetchSpaceUsingInfoUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute(usageId: UUID): UsageHistory {
        val userId = UUID.fromString("5e0df5ad-78e9-440e-a965-62fbb6375b59")
        return usageHistoryRepository
            .spaceUsingInfo(
                usageId = usageId,
                userId = userId,
            ).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND)
            }
    }
}
