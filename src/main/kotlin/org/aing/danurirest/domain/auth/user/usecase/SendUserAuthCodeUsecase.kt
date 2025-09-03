package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthenticationRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.aing.danurirest.global.util.GenerateRandomCode
import org.aing.danurirest.persistence.refreshToken.entity.VerifyCode
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.aing.danurirest.persistence.verify.repository.VerifyCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class SendUserAuthCodeUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val notificationService: NotificationService,
    private val verifyCodeRepository: VerifyCodeRepository,
) {
    fun execute(request: AuthenticationRequest) {
        val user = userJpaRepository.findByPhone(request.phone).orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (verifyCodeRepository.findByPhoneNumber(request.phone) != null) {
            throw CustomException(CustomErrorCode.ALREADY_SENT_VERIFY_CODE)
        }

        val authCode = GenerateRandomCode.execute()

        verifyCodeRepository.save(VerifyCode(authCode, request.phone))

        notificationService.sendNotification(
            toMessage = request.phone,
            template = MessageTemplate.VERIFICATION_CODE,
            params =
                MessageValueTemplate.VerificationParams(
                    orgName = user.company.name,
                    verificationCode = authCode,
                ),
        )
    }
}
