package org.aing.danurirest.domain.usage.usecase

import org.aing.danurirest.domain.common.dto.QrUsageIdRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
@Transactional
class FetchCheckOutUsecase(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val notificationService: NotificationService,
) {
    fun execute(request: QrUsageIdRequest) {
        val now = LocalDateTime.now()

        val result =
            usageHistoryJpaRepository
                .findById(request.usageId)
                .orElseThrow { CustomException(CustomErrorCode.NOT_FOUND) }

        if (result.endAt < now) {
            throw CustomException(CustomErrorCode.ALREADY_END)
        }

        result.endAt = LocalDateTime.now()

        notificationService.sendNotification(
            toMessage = result.user.phone,
            template = MessageTemplate.CHECKOUT_NOTIFICATION,
            params =
                MessageValueTemplate.CheckoutNotificationParams(
                    orgName = result.space.company.name,
                    spaceName = result.space.name,
                    usageDate = result.startAt.toLocalDate().toString(),
                    startTime =
                        result.startAt
                            .toLocalTime()
                            .truncatedTo(ChronoUnit.SECONDS)
                            .toString(),
                    endTime =
                        result.endAt!!
                            .toLocalTime()
                            .truncatedTo(ChronoUnit.SECONDS)
                            .toString(),
                ),
        )
    }
}
