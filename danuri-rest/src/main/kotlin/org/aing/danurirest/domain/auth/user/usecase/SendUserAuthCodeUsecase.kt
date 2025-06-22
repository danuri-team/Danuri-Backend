package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danuridomain.persistence.user.entity.UserAuthCode
import org.aing.danuridomain.persistence.user.repository.UserAuthCodeRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.discord.client.DiscordFeignClient
import org.aing.danurirest.global.third_party.discord.dto.DiscordMessage
import org.aing.danurirest.global.third_party.sms.SendSmsUsecase
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.Random

@Service
@Transactional(rollbackFor = [Exception::class])
class SendUserAuthCodeUsecase(
    private val userRepository: UserRepository,
    private val userAuthCodeRepository: UserAuthCodeRepository,
    private val sendSmsUsecase: SendSmsUsecase,
    @Value("\${spring.profiles.active:default}")
    private val activeProfile: String,
    private val discordFeignClient: DiscordFeignClient,
) {
    companion object {
        private const val AUTH_CODE_EXPIRE_MINUTES = 5L
    }

    fun execute(phone: String) {
        if (!userRepository.existsByPhone(phone)) {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

        userAuthCodeRepository.deleteByPhone(phone)

        val authCode = generateRandomCode()
        val expiredAt = LocalDateTime.now().plusMinutes(AUTH_CODE_EXPIRE_MINUTES)

        val userAuthCode =
            UserAuthCode(
                phone = phone,
                authCode = authCode,
                expiredAt = expiredAt,
            )
        userAuthCodeRepository.save(userAuthCode)

        val messageText = "[송정다누리청소년문화의집] 본인확인을 위해 인증번호 [$authCode]를 입력해 주세요."

        when (activeProfile) {
            "dev" -> discordFeignClient.sendMessage(DiscordMessage(messageText))
            "prod" -> sendSmsUsecase.execute(phone, messageText)
            else -> discordFeignClient.sendMessage(DiscordMessage("미확인 프로필입니다."))
        }
    }

    private fun generateRandomCode(): String {
        val random = Random()
        return String.format("%06d", random.nextInt(1000000))
    }
}
