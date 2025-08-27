package org.aing.danurirest.global.third_party.notification.service.impl

import org.aing.danurirest.global.third_party.notification.client.DiscordFeignClient
import org.aing.danurirest.global.third_party.notification.dto.DiscordMessage
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("dev")
class DiscordNotificationService(
    private val discordFeignClient: DiscordFeignClient,
) : NotificationService {
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

        discordFeignClient.sendMessage(DiscordMessage(content = discordMessageContent))
    }
}
