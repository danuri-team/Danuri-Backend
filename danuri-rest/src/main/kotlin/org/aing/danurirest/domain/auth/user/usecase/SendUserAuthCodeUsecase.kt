package org.aing.danurirest.domain.auth.user.usecase

import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException
import net.nurigo.sdk.message.model.Message
import org.aing.danuridomain.persistence.user.entity.UserAuthCode
import org.aing.danuridomain.persistence.user.repository.UserAuthCodeRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.Random

@Service
@Transactional(rollbackFor = [Exception::class])
class SendUserAuthCodeUsecase(
    private val userRepository: UserRepository,
    private val userAuthCodeRepository: UserAuthCodeRepository,
) {
    companion object {
        private const val AUTH_CODE_EXPIRE_MINUTES = 5L
    }

    fun execute(phone: String) {
        if (!userRepository.existsByPhone(phone)) {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

        userAuthCodeRepository.deleteByPhone(phone)

        val authCode = generateRandomCode()
        val expiredAt = LocalDateTime.now().plusMinutes(AUTH_CODE_EXPIRE_MINUTES)

        val userAuthCode =
            UserAuthCode(
                phone = phone,
                authCode = authCode,
                expiredAt = expiredAt,
            )
        userAuthCodeRepository.save(userAuthCode)
        val message = Message(from = "", to = phone, text = "[송정다누리센터] 인증번호는 $authCode 입니다. 본인이 아닐 경우, 문의 해주세요.")
        try {
            val messageService = NurigoApp.initialize(apiKey = "", apiSecretKey = "", domain = "https://api.solapi.com")
            messageService.send(message)
        } catch (e: NurigoMessageNotReceivedException) {
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    private fun generateRandomCode(): String {
        val random = Random()
        return String.format("%06d", random.nextInt(1000000))
    }
}
