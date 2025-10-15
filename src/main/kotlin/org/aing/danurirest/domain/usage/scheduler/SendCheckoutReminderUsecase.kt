package org.aing.danurirest.domain.usage.scheduler

import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.notification.template.MessageTemplate
import org.aing.danurirest.global.third_party.notification.template.MessageValueTemplate
import org.aing.danurirest.persistence.lock.repository.LockKeys
import org.aing.danurirest.persistence.lock.repository.LockRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class SendCheckoutReminderUsecase(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val lockRepository: LockRepository,
    private val notificationService: NotificationService,
) {
    @Transactional
    @Scheduled(fixedDelay = 300000)
    fun execute() {
        if (!lockRepository.lock(LockKeys.EXIT_ALERT_LOCK)) {
            return
        }
        try {
            processNotifications()
        } finally {
            lockRepository.unlock(LockKeys.EXIT_ALERT_LOCK)
        }
    }

    fun processNotifications() {
        val now = LocalDateTime.now()

        val inTimeRangeUsageHistory =
            usageHistoryJpaRepository.findByEndAtBetweenAndNotifiedAtIsNull(
                now,
                now.plusMinutes(5),
            )

        inTimeRangeUsageHistory.forEach { usage ->
            notificationService.sendNotification(
                toMessage = usage.user.phone,
                template = MessageTemplate.CHECKOUT_NOTIFICATION,
                params =
                    MessageValueTemplate.CheckoutNotificationParams(
                        orgName = usage.space.company.name,
                        spaceName = usage.space.name,
                        usageDate = usage.startAt.toLocalDate().toString(),
                        startTime =
                            usage.startAt
                                .toLocalTime()
                                .truncatedTo(ChronoUnit.SECONDS)
                                .toString(),
                        endTime =
                            usage.endAt
                                .toLocalTime()
                                .truncatedTo(ChronoUnit.SECONDS)
                                .toString(),
                    ),
            )

            usage.notifiedAt = now
        }
    }
}
