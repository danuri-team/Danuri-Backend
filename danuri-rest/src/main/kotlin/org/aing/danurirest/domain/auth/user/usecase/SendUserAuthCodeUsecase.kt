package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danuridomain.persistence.user.entity.UserAuthCode
import org.aing.danuridomain.persistence.user.repository.UserAuthCodeRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Random

@Service
class SendUserAuthCodeUsecase(
    private val userRepository: UserRepository,
    private val userAuthCodeRepository: UserAuthCodeRepository,
) {
    companion object {
        private const val AUTH_CODE_EXPIRE_MINUTES = 5L
    }

    fun execute(phone: String): String {
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

        // TODO: 뿌리오 연결
        return authCode
    }

    private fun generateRandomCode(): String {
        val random = Random()
        return String.format("%06d", random.nextInt(1000000))
    }
}
