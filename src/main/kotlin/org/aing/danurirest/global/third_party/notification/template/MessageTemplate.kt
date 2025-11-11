package org.aing.danurirest.global.third_party.notification.template

enum class MessageTemplate(
    val templateId: String,
    private val template: String,
) {
    VERIFICATION_CODE(
        templateId = "KA01TP250724083504813k3WOaqJ8PgO",
        template = "[#{기관명}] 본인확인을 위해 인증번호 [#{인증번호}]를 입력해 주세요.",
    ),

    SPACE_REGISTRATION(
        templateId = "KA01TP251110113312244j0OsqUaLY4z",
        template =
            """
            [#{기관명}] 공간 이용 등록

            공간 이용이 등록되었습니다 :)

            <이용권 정보>
            ► 공간명: #{공간명}
            ► 기간: #{이용일} #{시작시간} ~ #{종료시간}
            ► QR 바코드: #{링크}

            <공간 이용 시 안내사항>
            ► 이용시간을 잘 지켜주세요.
            ► 사용한 물건은 제자리를 찾아주세요.
            ► 음식물은 푸드존에서만 먹을 수 있어요.
            ► 큰 소리, 욕설은 하지않아요.
            ► 공간을 소중히 다뤄주세요.
            """.trimIndent(),
    ),

    CHECKOUT_NOTIFICATION(
        templateId = "KA01TP250717034805358qbFkm9Q1dT8",
        template =
            """
            [#{기관명}] 이용 종료 안내

            공간 이용이 종료되었습니다.
            이용해 주셔서 감사합니다.

            <이용권 정보>
            ► 공간명: #{공간명}
            ► 기간: #{이용일} #{시작시간} ~ #{종료시간}

            <연장 혹은 퇴실 가이드>
            ► 연장: 입실 하실 때 사용하신 디바이스를 통해 연장이 필요합니다.
            ► 퇴실: 이용 시간이 종료되어 바로 퇴실하시면 됩니다.
            """.trimIndent(),
    ),
    ;

    private fun format(parameters: Map<String, String>): String {
        var result = template
        parameters.forEach { (key, value) ->
            result = result.replace(key, value)
        }
        return result
    }

    fun formatMessage(params: MessageValueTemplate): String = format(params.toMap())
}
