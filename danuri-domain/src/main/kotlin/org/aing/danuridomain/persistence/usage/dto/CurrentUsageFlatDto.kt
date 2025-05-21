package org.aing.danuridomain.persistence.usage.dto

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
    val itemName: String?,
    val quantity: Int?,
    val returnedQuantity: Int?,
)
