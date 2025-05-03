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
    val companyId: UUID,
    val companyName: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime?,
    val rentalCount: Int
) {
    companion object {
        fun from(entity: UsageHistory): UsageHistoryResponse =
            UsageHistoryResponse(
                id = entity.id!!,
                userId = entity.user.id!!,
                userName = entity.user.name,
                userPhone = entity.user.phone,
                spaceId = entity.space.id!!,
                spaceName = entity.space.id.toString(),
                companyId = entity.space.company.id!!,
                companyName = entity.space.company.name,
                startAt = entity.start_at,
                endAt = entity.end_at,
                rentalCount = entity.rental.size
            )
    }
} 