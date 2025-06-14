package org.aing.danurirest.domain.admin.dto

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

data class UsageHistoryResponse(
    val id: UUID,
    val userId: UUID,
    val userName: String,
    val userPhone: String,
    val spaceId: UUID,
    val spaceName: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime?,
    val rentalCount: Int,
) {
    companion object {
        fun from(entity: UsageHistory): UsageHistoryResponse =
            UsageHistoryResponse(
                id = entity.id!!,
                userId = entity.user.id!!,
                userName = entity.user.name,
                userPhone = entity.user.phone,
                spaceId = entity.space.id!!,
                spaceName = entity.space.name,
                startAt = entity.startAt,
                endAt = entity.endAt,
                rentalCount = entity.rental.size,
            )
    }
}
