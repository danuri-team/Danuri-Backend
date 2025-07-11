package org.aing.danurirest.persistence.usage.dto

import java.time.LocalDateTime
import java.util.UUID

data class DetailUsageInfo(
    val spaceId: UUID,
    val usageId: UUID,
    val spaceName: String,
    val rentalItem: List<DetailRentedItemInfo>,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime?,
)
