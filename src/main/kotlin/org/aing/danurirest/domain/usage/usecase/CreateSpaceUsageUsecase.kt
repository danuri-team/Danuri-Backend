package org.aing.danurirest.domain.usage.usecase

import org.aing.danurirest.domain.usage.dto.CreateSpaceUsageRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.aing.danurirest.global.third_party.s3.BucketType
import org.aing.danurirest.global.third_party.s3.service.S3Service
import org.aing.danurirest.global.third_party.shortUrl.service.ShortUrlService
import org.aing.danurirest.global.util.GenerateQrCode
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.aing.danurirest.persistence.usage.entity.AdditionalParticipant
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.usage.repository.AdditionalParticipantJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class CreateSpaceUsageUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val spaceJpaRepository: SpaceJpaRepository,
    private val userJpaRepository: UserJpaRepository,
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val additionalParticipantJpaRepository: AdditionalParticipantJpaRepository,
    private val notificationService: NotificationService,
    private val s3Service: S3Service,
    private val shortUrlService: ShortUrlService,
) {
    private val log: Logger = LoggerFactory.getLogger(CreateSpaceUsageUsecase::class.java)

    @Transactional
    fun execute(request: CreateSpaceUsageRequest) {
        val context = PrincipalUtil.getContextDto()
        val userId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        val space = findSpaceById(request.spaceId)
        val startTime = LocalDateTime.of(LocalDate.now(), request.startAt)
        val endReserveTime =
            request.startAt
                .truncatedTo(ChronoUnit.HOURS)
                .plusMinutes(if (request.startAt.minute < 30) 30 else 60)
        val endTime =
            if (endReserveTime.isBefore(request.startAt)) {
                LocalDateTime.of(LocalDate.now().plusDays(1), endReserveTime)
            } else {
                LocalDateTime.of(LocalDate.now(), endReserveTime)
            }

        checkUserCurrentUsage(userId)

        checkSpaceAvailableTime(space, startTime, endTime)

        if (!space.allowOverlap) {
            checkSpaceCurrentUsage(request.spaceId, startTime, endTime)
        }

        createSpaceUsage(space, userId, startTime, endTime, request)
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
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        val today = startTime.toLocalDate()

        val spaceStartTime = LocalDateTime.of(today, space.startAt)
        val spaceEndTime = LocalDateTime.of(today, space.endAt)

        val adjustedSpaceEndTime =
            if (space.endAt == LocalTime.MIDNIGHT || space.endAt < space.startAt) {
                spaceEndTime.plusDays(1)
            } else {
                spaceEndTime
            }

        if (startTime < spaceStartTime || endTime > adjustedSpaceEndTime) {
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
        request: CreateSpaceUsageRequest,
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

        request.additionalParticipants.forEach { dto ->
            additionalParticipantJpaRepository.save(
                AdditionalParticipant(
                    usageHistory = usage,
                    sex = dto.sex,
                    ageGroup = dto.ageGroup,
                    count = dto.count,
                ),
            )
        }

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

        val shortQrLink =
            try {
                shortUrlService.execute(qrLink)
            } catch (e: Exception) {
                log.warn("QR 링크 단축에 실패하여 원본 링크를 사용합니다. error: {}", e.message)
                qrLink
            }

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
                    qrLink = shortQrLink,
                ),
        )
    }
}
