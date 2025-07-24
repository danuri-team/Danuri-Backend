package org.aing.danurirest.global.third_party.sms

import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.model.KakaoOption
import net.nurigo.sdk.message.model.Message
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SendKakaoUsecase(
    @Value("\${sms.apiKey}")
    private val solApiKey: String,
    @Value("\${sms.apiSecret}")
    private val solApiSecretKey: String,
    @Value("\${sms.fromNumber}")
    private val solFrom: String,
    @Value("\${sms.pfId}")
    private val solPfId: String,
) {
    private val messageService =
        NurigoApp.initialize(
            apiKey = solApiKey,
            apiSecretKey = solApiSecretKey,
            domain = "https://api.solapi.com",
        )

    fun execute(
        phone: String,
        template: String,
        info: HashMap<String, String>,
    ) {
        val kakaoOption = KakaoOption()

        kakaoOption.apply {
            pfId = solPfId
            templateId = template
            variables = info
        }

        val message =
            Message(
                from = solFrom,
                to = phone.replace("-", ""),
                kakaoOptions = kakaoOption,
            )

        try {
            messageService.send(message)
        } catch (e: Exception) {
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }
}
