package org.aing.danurirest.persistence.usage.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime
import java.util.UUID

@QueryProjection
data class CurrentUsageFlatDto(
    val userId: UUID,
    val spaceId: UUID,
    val spaceName: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime?,
    val usageId: UUID,
    val itemName: String?,
    val quantity: Int?,
    val returnedQuantity: Int?,
)
