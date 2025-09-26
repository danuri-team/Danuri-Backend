package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthenticationRequest
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.aing.danurirest.global.util.GenerateRandomCode
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.verify.VerifyCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SendPasswordResetMessageUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val verifyCodeRepository: VerifyCodeRepository,
    private val notificationService: NotificationService,
) {
    @Transactional
    fun execute(request: AuthenticationRequest) {
        val admin: Optional<Admin> =
            adminJpaRepository.findByPhone(request.phone)

        admin.ifPresent { result ->
            run {
                val authCode = GenerateRandomCode.execute()

                verifyCodeRepository.save(
                    phoneNumber = result.phone,
                    code = authCode,
                )

                notificationService.sendNotification(
                    toMessage = result.phone,
                    template = MessageTemplate.VERIFICATION_CODE,
                    params =
                        MessageValueTemplate.VerificationParams(
                            orgName = result.company.name,
                            verificationCode = authCode,
                        ),
                )
            }
        }
    }
}
