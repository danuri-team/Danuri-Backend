package org.aing.danurirest.domain.space.usecase

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import org.aing.danurirest.domain.space.dto.UseSpaceRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.aing.danurirest.global.third_party.discord.client.DiscordFeignClient
import org.aing.danurirest.global.third_party.discord.dto.DiscordMessage
import org.aing.danurirest.global.third_party.s3.service.S3Service
import org.aing.danurirest.global.third_party.sms.SendKakaoUsecase
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class CreateSpaceUsageUsecase(
    private val usageHistoryRepository: UsageHistoryRepository,
    private val spaceJpaRepository: SpaceJpaRepository,
    private val userJpaRepository: UserJpaRepository,
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val kakaoUsecase: SendKakaoUsecase,
    private val discordFeignClient: DiscordFeignClient,
    @Value("\${spring.profiles.active:default}")
    private val activeProfile: String,
    private val s3Service: S3Service,
) {
    companion object {
        private const val USAGE_DURATION_MINUTES = 30L
        private const val WIDTH = 200
        private const val HEIGHT = 200
    }

    private val log: Logger = LoggerFactory.getLogger(CreateSpaceUsageUsecase::class.java)

    @Transactional
    fun execute(useSpaceRequest: UseSpaceRequest): Boolean {
        val context = getCurrentContext()
        val userId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

        val space = findSpaceById(useSpaceRequest.spaceId)
        val now = LocalDateTime.now()
        val endTime = now.plusMinutes(USAGE_DURATION_MINUTES)

        // 1. 현재 사용자의 중복 예약 검사
        checkUserCurrentUsage(userId)

        // 2. 공간 사용 가능 시간 검사
        checkSpaceAvailableTime(space, now)

        // 3. 공간 중복 예약 검사
        checkSpaceCurrentUsage(useSpaceRequest.spaceId, now, endTime)

        createSpaceUsage(space, userId, now, endTime)

        return true
    }

    private fun findSpaceById(spaceId: UUID): Space =
        spaceJpaRepository
            .findById(spaceId)
            .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

    // 사용자의 현재 사용 중인 예약이 있는지 확인
    private fun checkUserCurrentUsage(userId: UUID) {
        val userCurrentUsages =
            usageHistoryRepository.findUserCurrentUsageInfo(
                userId = userId,
            )

        if (userCurrentUsages.isUsingSpace) {
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
            usageHistoryJpaRepository.spaceUsingTime(
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
            userJpaRepository
                .findById(userId)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        val usage =
            usageHistoryJpaRepository.save(
                UsageHistory(
                    user = user,
                    space = space,
                    startAt = startTime,
                    endAt = endTime,
                ),
            )

        val qr = createQr("{\"usageId\": " + usage.id + "}")

        val fileName =
            s3Service.uploadQrImage(
                qr.getOrElse {
                    throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
                },
                "danuri-cloud",
            )

        val qrLink = s3Service.generatePreSignedUrl(bucketName = "danuri-cloud", fileName)

        val info: HashMap<String, String> = hashMapOf()

        info["#{기관명}"] = "다누리"
        info["#{공간명}"] = space.name
        info["#{이용일}"] = startTime.toLocalDate().toString()
        info["#{시작시간}"] = startTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString()
        info["#{종료시간}"] = endTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString()
        info["#{링크}"] = qrLink

        when (activeProfile) {
            "dev" ->
                discordFeignClient.sendMessage(
                    DiscordMessage(
                        content =
                            "[${info["#{기관명}"]}] 공간 이용 등록\n" +
                                "\n" +
                                "공간 이용을 등록해 주셔서 감사합니다 :)\n" +
                                "\n" +
                                "<이용권 정보>\n" +
                                "► 공간명: ${info["#{공간명}"]}\n" +
                                "► 기간: ${info["#{이용일}"]} ${info["#{시작시간}"]} ~ ${info["#{종료시간}"]}\n" +
                                "► QR 바코드: ${info["#{링크}"]}",
                    ),
                )
            "prod" ->
                kakaoUsecase.execute(
                    phone = user.phone,
                    template = "KA01TP250717034426750fYNSMbPdfdc",
                    info = info,
                )
            else -> discordFeignClient.sendMessage(DiscordMessage("미확인 프로필입니다."))
        }
    }

    private fun getCurrentContext(): ContextDto = PrincipalUtil.getContextDto()

    fun createQr(usageId: String): Result<ByteArray> =
        try {
            val encode = MultiFormatWriter().encode(usageId, BarcodeFormat.QR_CODE, WIDTH, HEIGHT)
            val out = ByteArrayOutputStream()
            MatrixToImageWriter.writeToStream(encode, "JPG", out)
            Result.success(out.toByteArray())
        } catch (e: Exception) {
            log.error(e.message, e)
            Result.failure(e)
        }
}
