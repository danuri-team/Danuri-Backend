package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthenticationRequest
import org.aing.danurirest.global.common.GenerateRandomCode
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.discord.client.DiscordFeignClient
import org.aing.danurirest.global.third_party.discord.dto.DiscordMessage
import org.aing.danurirest.global.third_party.sms.SendSmsUsecase
import org.aing.danurirest.persistence.user.entity.UserAuthCode
import org.aing.danurirest.persistence.user.repository.UserAuthCodeRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(rollbackFor = [Exception::class])
class SendUserAuthCodeUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val userAuthCodeRepository: UserAuthCodeRepository,
    private val sendSmsUsecase: SendSmsUsecase,
    @Value("\${spring.profiles.active:default}")
    private val activeProfile: String,
    private val discordFeignClient: DiscordFeignClient,
) {
    companion object {
        private const val AUTH_CODE_EXPIRE_MINUTES = 5L
    }

    fun execute(request: AuthenticationRequest) {
        if (!userJpaRepository.existsByPhone(request.phone)) {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

        userAuthCodeRepository.deleteByPhone(request.phone)

        val authCode = GenerateRandomCode.execute()
        val expiredAt = LocalDateTime.now().plusMinutes(AUTH_CODE_EXPIRE_MINUTES)

        val messageText = "[송정다누리청소년문화의집] 본인확인을 위해 인증번호 [$authCode]를 입력해 주세요."

        when (activeProfile) {
            "dev" -> discordFeignClient.sendMessage(DiscordMessage(messageText))
            "prod" -> sendSmsUsecase.execute(request.phone, messageText)
            else -> discordFeignClient.sendMessage(DiscordMessage("미확인 프로필입니다."))
        }

        val userAuthCode =
            UserAuthCode(
                phone = request.phone,
                authCode = authCode,
                expiredAt = expiredAt,
            )

        userAuthCodeRepository.save(userAuthCode)
    }
}
