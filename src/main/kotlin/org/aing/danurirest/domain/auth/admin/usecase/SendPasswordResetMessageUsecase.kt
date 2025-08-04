package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthenticationRequest
import org.aing.danurirest.global.common.GenerateRandomCode
import org.aing.danurirest.global.third_party.discord.client.DiscordFeignClient
import org.aing.danurirest.global.third_party.discord.dto.DiscordMessage
import org.aing.danurirest.global.third_party.sms.SendSmsUsecase
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.user.entity.UserAuthCode
import org.aing.danurirest.persistence.user.repository.UserAuthCodeJpaRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional(rollbackFor = [Exception::class])
class SendPasswordResetMessageUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val sendSmsUsecase: SendSmsUsecase,
    @Value("\${spring.profiles.active:default}")
    private val activeProfile: String,
    private val discordFeignClient: DiscordFeignClient,
    private val userAuthCodeJpaRepository: UserAuthCodeJpaRepository,
) {
    companion object {
        private const val AUTH_CODE_EXPIRE_MINUTES = 5L
    }

    fun execute(request: AuthenticationRequest) {
        val admin: Optional<Admin> =
            adminJpaRepository.findByPhone(request.phone)

        admin.ifPresent { result ->
            run {
                val authCode = GenerateRandomCode.execute()
                val messageText = "[송정다누리청소년문화의집] 본인확인을 위해 인증번호 [$authCode]를 입력해 주세요."
                val expiredAt = LocalDateTime.now().plusMinutes(AUTH_CODE_EXPIRE_MINUTES)

                when (activeProfile) {
                    "dev" -> discordFeignClient.sendMessage(DiscordMessage(messageText))
                    "prod" -> sendSmsUsecase.execute(result.phone, messageText)
                    else -> discordFeignClient.sendMessage(DiscordMessage("미확인 프로필입니다."))
                }

                val userAuthCode =
                    UserAuthCode(
                        phone = result.phone,
                        authCode = authCode,
                        expiredAt = expiredAt,
                    )

                userAuthCodeJpaRepository.save(userAuthCode)
            }
        }
    }
}
