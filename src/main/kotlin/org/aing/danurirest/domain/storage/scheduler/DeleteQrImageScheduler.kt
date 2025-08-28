package org.aing.danurirest.domain.storage.scheduler

import org.aing.danurirest.global.third_party.s3.BucketType
import org.aing.danurirest.global.third_party.s3.service.S3Service
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DeleteQrImageScheduler(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
    private val s3Service: S3Service,
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun execute() {
        val usageList = usageHistoryJpaRepository.findIdsByEndAtBetween()
        s3Service.deleteFiles(
            BucketType.QR_LINK,
            usageList,
        )
    }
}
