package org.aing.danurirest.global.third_party.sms.template

sealed class MessageValueTemplate {
    abstract fun toMap(): Map<String, String>

    data class VerificationParams(
        val orgName: String,
        val verificationCode: String,
    ) : MessageValueTemplate() {
        override fun toMap(): Map<String, String> =
            mapOf(
                "#{기관명}" to orgName,
                "#{인증번호}" to verificationCode,
            )
    }

    data class SpaceRegistrationParams(
        val orgName: String,
        val spaceName: String,
        val usageDate: String,
        val startTime: String,
        val endTime: String,
        val qrLink: String,
    ) : MessageValueTemplate() {
        override fun toMap(): Map<String, String> =
            mapOf(
                "#{기관명}" to orgName,
                "#{공간명}" to spaceName,
                "#{이용일}" to usageDate,
                "#{시작시간}" to startTime,
                "#{종료시간}" to endTime,
                "#{링크}" to qrLink,
            )
    }

    data class CheckoutNotificationParams(
        val orgName: String,
        val spaceName: String,
        val usageDate: String,
        val startTime: String,
        val endTime: String,
    ) : MessageValueTemplate() {
        override fun toMap(): Map<String, String> =
            mapOf(
                "#{기관명}" to orgName,
                "#{공간명}" to spaceName,
                "#{이용일}" to usageDate,
                "#{시작시간}" to startTime,
                "#{종료시간}" to endTime,
            )
    }
}
