package org.aing.danurirest.global.third_party.notification.service.impl

import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.model.KakaoOption
import net.nurigo.sdk.message.model.Message
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("prod")
class KakaoNotificationService(
    @Value("\${sms.apiKey}")
    private val solApiKey: String,
    @Value("\${sms.apiSecret}")
    private val solApiSecretKey: String,
    @Value("\${sms.fromNumber}")
    private val solFrom: String,
    @Value("\${sms.pfId}")
    private val solPfId: String,
) : NotificationService {
    private val log: Logger = LoggerFactory.getLogger(KakaoNotificationService::class.java)

    private val messageService =
        NurigoApp.initialize(
            apiKey = solApiKey,
            apiSecretKey = solApiSecretKey,
            domain = "https://api.solapi.com",
        )

    override fun sendNotification(
        toMessage: String,
        template: MessageTemplate,
        params: MessageValueTemplate,
    ) {
        val kakaoOption = KakaoOption()

        kakaoOption.apply {
            pfId = solPfId
            templateId = template.templateId
            variables = params.toMap() as HashMap<String, String>
        }

        val message =
            Message(
                from = solFrom,
                to = toMessage.replace("-", ""),
                kakaoOptions = kakaoOption,
            )

        try {
            messageService.send(message)
        } catch (e: Exception) {
            log.error("[비동기 작업 에러] 카카오톡 알림 발송에 실패했어요. ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }
}
