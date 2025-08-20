package org.aing.danurirest.domain.admin.dto

import java.time.LocalDateTime
import java.util.UUID

data class CreateUsageHistoryRequest(
    val userId: UUID,
    val spaceId: UUID,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
)

data class UpdateUsageHistoryRequest(
    val endAt: LocalDateTime,
) 
