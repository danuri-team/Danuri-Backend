package org.aing.danurirest.domain.admin.dto

import org.aing.danurirest.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

data class UsageHistoryResponse(
    val id: UUID,
    val userId: UUID,
    val userPhone: String,
    val spaceId: UUID,
    val spaceName: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime?,
    val formResult: String,
    val rentalCount: Int,
) {
    companion object {
        fun from(usageHistory: UsageHistory): UsageHistoryResponse =
            UsageHistoryResponse(
                id = usageHistory.id!!,
                userId = usageHistory.user.id!!,
                userPhone = usageHistory.user.phone,
                formResult = usageHistory.user.signUpForm?.result!!,
                spaceId = usageHistory.space.id!!,
                spaceName = usageHistory.space.name,
                startAt = usageHistory.startAt,
                endAt = usageHistory.endAt,
                rentalCount = usageHistory.rental.size,
            )
    }
}
