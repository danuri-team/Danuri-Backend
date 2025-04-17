package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreateSpaceUsageUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val spaceRepository: SpaceRepository,
) {
    fun execute(spaceId: UUID): Boolean {
        val space: Space =
            spaceRepository.findById(spaceId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND)
            }
        val usageTime: List<UsageHistory> =
            usageHistoryRepository.spaceUsingTime(
                spaceId = spaceId,
            )
        val now = LocalDateTime.now()
        val nowTime = now.toLocalTime()

        val isOverlapping =
            usageTime.any { ut ->
                val inUsageTime = !ut.start_at.isAfter(now) && (ut.end_at?.isAfter(now) ?: false)

                val timeInRange =
                    !space.start_at.isAfter(nowTime) &&
                        !space.end_at.isBefore(nowTime.plusMinutes(30))

                inUsageTime && timeInRange
            }

        if (isOverlapping) {
            throw CustomException(CustomErrorCode.USAGE_CONFLICT_SPACE)
        }

        usageHistoryRepository.createSpaceUsage(
            space = space,
            user = TODO(),
        )

        return true
    }
}
