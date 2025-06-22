package org.aing.danurirest.domain.space.usecase

import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.domain.space.dto.UseSpaceRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
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

    fun execute(useSpaceRequest: UseSpaceRequest): Boolean {
        val context = getCurrentContext()
        val userId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        val space = findSpaceById(useSpaceRequest.spaceId)
        val now = LocalDateTime.now()
        val endTime = now.plusMinutes(USAGE_DURATION_MINUTES)

        // 1. 현재 사용자의 중복 예약 검사
        checkUserCurrentUsage(userId, now)

        // 2. 공간 사용 가능 시간 검사
        checkSpaceAvailableTime(space, now)

        // 3. 공간 중복 예약 검사
        checkSpaceCurrentUsage(useSpaceRequest.spaceId, now, endTime)

        createSpaceUsage(space, userId, now, endTime)

        return true
    }

    private fun findSpaceById(spaceId: UUID): Space =
        spaceRepository
            .findById(spaceId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

    // 사용자의 현재 사용 중인 예약이 있는지 확인
    private fun checkUserCurrentUsage(
        userId: UUID,
        now: LocalDateTime,
    ) {
        val userCurrentUsages =
            usageHistoryRepository.findAllByUserIdAndDateRange(
                userId = userId,
                currentTime = LocalDateTime.now(),
            )

        val hasActiveUsage =
            userCurrentUsages.any { usage ->
                !usage.startAt.isAfter(now) && (usage.endAt == null || !now.isAfter(usage.endAt))
            }

        if (hasActiveUsage) {
            throw CustomException(CustomErrorCode.USAGE_CONFLICT_USER)
        }
    }

    // 공간 가용 시간 확인
    private fun checkSpaceAvailableTime(
        space: Space,
        now: LocalDateTime,
    ) {
        val nowTime = now.toLocalTime()

        if (nowTime.isBefore(space.startAt) || nowTime.isAfter(space.endAt)) {
            throw CustomException(CustomErrorCode.SPACE_NOT_AVAILABLE)
        }
    }

    // 공간 중복 예약 확인
    private fun checkSpaceCurrentUsage(
        spaceId: UUID,
        now: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        val currentUsages =
            usageHistoryRepository.spaceUsingTime(
                spaceId = spaceId,
                startTime = now.minusMinutes(USAGE_DURATION_MINUTES),
                endTime = endTime.plusMinutes(USAGE_DURATION_MINUTES),
            )

        val isOverlapping =
            currentUsages.any { usage ->
                (usage.startAt <= endTime) &&
                    (usage.endAt == null || usage.endAt?.isAfter(now) == true || usage.endAt?.isEqual(now) == true)
            }

        if (isOverlapping) {
            throw CustomException(CustomErrorCode.USAGE_CONFLICT_SPACE)
        }
    }

    private fun createSpaceUsage(
        space: Space,
        userId: UUID,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        val user =
            userRepository
                .findById(userId)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        usageHistoryRepository.createSpaceUsage(space, user, startTime, endTime)
    }

    private fun getCurrentContext(): ContextDto {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as? ContextDto
            ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
    }
}
