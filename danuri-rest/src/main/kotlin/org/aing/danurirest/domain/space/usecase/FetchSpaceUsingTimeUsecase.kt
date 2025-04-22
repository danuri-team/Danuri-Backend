package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FetchSpaceUsingTimeUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val spaceRepository: SpaceRepository,
) {
    fun execute(spaceId: UUID): List<UsageHistory> {
        validateSpaceExists(spaceId)
        return findSpaceUsingTime(spaceId)
    }

    private fun validateSpaceExists(spaceId: UUID) {
        if (!spaceRepository.existsById(spaceId)) {
            throw CustomException(CustomErrorCode.NOT_FOUND)
        }
    }

    private fun findSpaceUsingTime(spaceId: UUID): List<UsageHistory> = usageHistoryRepository.spaceUsingTime(spaceId)
}
