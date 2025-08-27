package org.aing.danurirest.domain.auth.admin.usecase

import org.aing.danurirest.domain.auth.common.dto.AuthenticationRequest
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.aing.danurirest.global.util.GenerateRandomCode
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.user.entity.UserAuthCode
import org.aing.danurirest.persistence.user.repository.UserAuthCodeJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional(rollbackFor = [Exception::class])
class SendPasswordResetMessageUsecase(
    private val adminJpaRepository: AdminJpaRepository,
    private val userAuthCodeJpaRepository: UserAuthCodeJpaRepository,
    private val notificationService: NotificationService,
) {
    companion object {
        private const val AUTH_CODE_EXPIRE_MINUTES = 5L
    }

    fun execute(request: AuthenticationRequest) {
        val admin: Optional<Admin> =
            adminJpaRepository.findByPhone(request.phone)

        admin.ifPresent { result ->
            run {
                val authCode = GenerateRandomCode.execute()
                val expiredAt = LocalDateTime.now().plusMinutes(AUTH_CODE_EXPIRE_MINUTES)

                notificationService.sendNotification(
                    toMessage = result.phone,
                    template = MessageTemplate.VERIFICATION_CODE,
                    params =
                        MessageValueTemplate.VerificationParams(
                            orgName = result.company.name,
                            verificationCode = authCode,
                        ),
                )

                val userAuthCode =
                    UserAuthCode(
                        phone = result.phone,
                        authCode = authCode,
                        expiredAt = expiredAt,
                    )

                userAuthCodeJpaRepository.save(userAuthCode)
            }
        }
    }
}
