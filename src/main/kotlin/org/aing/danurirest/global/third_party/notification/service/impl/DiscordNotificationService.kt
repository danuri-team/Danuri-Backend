package org.aing.danurirest.global.third_party.notification.service.impl

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.notification.client.DiscordFeignClient
import org.aing.danurirest.global.third_party.notification.dto.DiscordMessage
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("dev")
class DiscordNotificationService(
    private val discordFeignClient: DiscordFeignClient,
) : NotificationService {
    private val log: Logger = LoggerFactory.getLogger(DiscordNotificationService::class.java)

    override fun sendNotification(
        toMessage: String,
        template: MessageTemplate,
        params: MessageValueTemplate,
    ) {
        val formattedMessage = template.formatMessage(params)

        val discordMessageContent =
            """
            [DEV] 메시지 발송 (수신자: $toMessage)
            ---
            $formattedMessage
            """.trimIndent()
        try {
            discordFeignClient.sendMessage(DiscordMessage(content = discordMessageContent))
        } catch (e: Exception) {
            log.error("[비동기 작업 에러] 카카오톡 알림 발송에 실패했어요. ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }
}
