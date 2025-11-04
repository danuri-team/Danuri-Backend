package org.aing.danurirest.domain.admin.dto

import java.time.LocalDateTime
import java.util.UUID

data class UsageHistorySearchRequest(
    val spaceId: UUID? = null,
    val userId: UUID? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
)
