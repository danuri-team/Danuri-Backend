package org.aing.danurirest.global.third_party.sms

import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.model.Message
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SendSmsUsecase(
    @Value("\${sms.apiKey}")
    private val solApiKey: String,
    @Value("\${sms.apiSecret}")
    private val solApiSecretKey: String,
    @Value("\${sms.fromnumber}")
    private val solfrom: String,
) {
    fun execute(
        phone: String,
        text: String,
    ) {
        val message =
            Message(
                from = solfrom,
                to = phone.replace("-", ""),
                text = text,
            )

        try {
            val messageService =
                NurigoApp.initialize(
                    apiKey = solApiKey,
                    apiSecretKey = solApiSecretKey,
                    domain = "https://api.solapi.com",
                )
            messageService.send(message)
        } catch (e: Exception) {
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }
}
