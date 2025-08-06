package org.aing.danurirest.global.third_party.notification.service

import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.springframework.scheduling.annotation.Async

interface NotificationService {
    @Async
    fun sendNotification(
        toMessage: String,
        template: MessageTemplate,
        params: MessageValueTemplate,
    )
}
