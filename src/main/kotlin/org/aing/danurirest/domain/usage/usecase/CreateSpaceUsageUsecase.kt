package org.aing.danurirest.domain.usage.usecase

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.aing.danurirest.global.third_party.s3.BucketType
import org.aing.danurirest.global.third_party.s3.service.S3Service
import org.aing.danurirest.global.util.GenerateQrCode
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class CreateSpaceUsageUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val spaceJpaRepository: SpaceJpaRepository,
    private val userJpaRepository: UserJpaRepository,
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val notificationService: NotificationService,
    private val s3Service: S3Service,
) {
    companion object {
        private const val USAGE_DURATION_MINUTES = 30L
    }

    @Transactional
    fun execute(spaceId: UUID) {
        val context = PrincipalUtil.getContextDto()
        val userId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        val space = findSpaceById(spaceId)
        val now = LocalDateTime.now()
        val endTime = now.plusMinutes(USAGE_DURATION_MINUTES)

        checkUserCurrentUsage(userId)

        checkSpaceAvailableTime(space, now)

        checkSpaceCurrentUsage(spaceId, now, endTime)

        createSpaceUsage(space, userId, now, endTime)
    }

    private fun findSpaceById(spaceId: UUID): Space =
        spaceJpaRepository
            .findById(spaceId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

    private fun checkUserCurrentUsage(userId: UUID) {
        val userCurrentUsages =
            usageHistoryRepository.findUserCurrentUsageInfo(
                userId = userId,
            )

        if (userCurrentUsages.isUsingSpace) {
            throw CustomException(CustomErrorCode.USAGE_CONFLICT_USER)
        }
    }

    private fun checkSpaceAvailableTime(
        space: Space,
        now: LocalDateTime,
    ) {
        val nowTime = now.toLocalTime()

        if (!(nowTime.isAfter(space.startAt) && nowTime.isBefore(space.endAt))) {
            throw CustomException(CustomErrorCode.SPACE_NOT_AVAILABLE)
        }
    }

    private fun checkSpaceCurrentUsage(
        spaceId: UUID,
        now: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        val currentUsages =
            usageHistoryJpaRepository.findUsagesBySpaceAndTimeRange(
                spaceId = spaceId,
                startTime = now,
                endTime = endTime,
            )

        val isOverlapping = currentUsages.isNotEmpty()

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
            userJpaRepository
                .findById(userId)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.signUpForm == null) {
            throw CustomException(CustomErrorCode.NOT_SIGNED_UP)
        }

        val usage =
            usageHistoryJpaRepository.save(
                UsageHistory(
                    user = user,
                    space = space,
                    startAt = startTime,
                    endAt = endTime,
                ),
            )

        val qr = GenerateQrCode.execute("""{"usageId":"${usage.id}"}""")

        val fileName =
            s3Service.uploadQrImage(
                qr.getOrElse {
                    throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
                },
                BucketType.QR_LINK,
            )

        val qrLink =
            s3Service.generatePreSignedUrl(
                BucketType.QR_LINK,
                fileName,
            )

        notificationService.sendNotification(
            toMessage = user.phone,
            template = MessageTemplate.SPACE_REGISTRATION,
            params =
                MessageValueTemplate.SpaceRegistrationParams(
                    orgName = space.company.name,
                    spaceName = space.name,
                    usageDate = usage.startAt.toLocalDate().toString(),
                    startTime =
                        usage.startAt
                            .toLocalTime()
                            .truncatedTo(ChronoUnit.SECONDS)
                            .toString(),
                    endTime =
                        usage.endAt
                            .toLocalTime()
                            .truncatedTo(ChronoUnit.SECONDS)
                            .toString(),
                    qrLink = qrLink,
                ),
        )
    }
}
