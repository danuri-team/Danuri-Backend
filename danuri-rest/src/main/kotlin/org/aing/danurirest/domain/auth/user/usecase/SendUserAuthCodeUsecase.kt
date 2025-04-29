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
        // 해당 전화번호를 가진 사용자가 있는지 확인
        if (!userRepository.existsByPhone(phone)) {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }

        // 기존 인증 코드가 있으면 삭제
        userAuthCodeRepository.deleteByPhone(phone)

        // 인증 코드 생성 (6자리 숫자)
        val authCode = generateRandomCode()
        val expiredAt = LocalDateTime.now().plusMinutes(AUTH_CODE_EXPIRE_MINUTES)

        // 인증 코드 저장
        val userAuthCode =
            UserAuthCode(
                phone = phone,
                authCode = authCode,
                expiredAt = expiredAt,
            )
        userAuthCodeRepository.save(userAuthCode)

        return authCode
    }

    private fun generateRandomCode(): String {
        val random = Random()
        return String.format("%06d", random.nextInt(1000000))
    }
}
