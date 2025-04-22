package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Service
class CreateSpaceUsageUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val spaceRepository: SpaceRepository,
    private val userRepository: UserRepository,
) {
    companion object {
        private const val USAGE_DURATION_MINUTES = 30L
    }

    fun execute(spaceId: UUID): Boolean {
        val space = findSpaceById(spaceId)
        val currentUsage = findCurrentUsage(spaceId)
        val now = LocalDateTime.now()

        validateUsageTime(space, currentUsage, now)

        val user = findCurrentUser()
        createSpaceUsage(space, user)

        return true
    }

    private fun findSpaceById(spaceId: UUID): Space =
        spaceRepository
            .findById(spaceId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND) }

    private fun findCurrentUsage(spaceId: UUID): List<UsageHistory> = usageHistoryRepository.spaceUsingTime(spaceId)

    private fun validateUsageTime(
        space: Space,
        currentUsage: List<UsageHistory>,
        now: LocalDateTime,
    ) {
        val nowTime = now.toLocalTime()

        val isTimeInRange = isTimeInRange(space.start_at, space.end_at, nowTime)
        val isOverlapping = isOverlappingWithCurrentUsage(currentUsage, now)

        if (!isTimeInRange) {
            throw CustomException(CustomErrorCode.USAGE_CONFLICT_SPACE)
        }

        if (isOverlapping) {
            throw CustomException(CustomErrorCode.USAGE_CONFLICT_SPACE)
        }
    }

    private fun isTimeInRange(
        startTime: LocalTime,
        endTime: LocalTime,
        currentTime: LocalTime,
    ): Boolean = !startTime.isAfter(currentTime) && !endTime.isBefore(currentTime.plusMinutes(USAGE_DURATION_MINUTES))

    private fun isOverlappingWithCurrentUsage(
        usageTime: List<UsageHistory>,
        now: LocalDateTime,
    ): Boolean =
        usageTime.any { ut ->
            !ut.start_at.isAfter(now) && (ut.end_at?.isAfter(now) ?: false)
        }

    private fun findCurrentUser(): ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto

    private fun createSpaceUsage(
        space: Space,
        userContext: ContextDto,
    ) {
        val user =
            userRepository
                .findById(userContext.id!!)
                .orElseThrow { CustomException(CustomErrorCode.VALIDATION_ERROR) }

        usageHistoryRepository.createSpaceUsage(space, user)
    }
}
