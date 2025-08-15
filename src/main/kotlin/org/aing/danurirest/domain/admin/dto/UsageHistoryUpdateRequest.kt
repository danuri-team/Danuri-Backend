package org.aing.danurirest.domain.admin.dto

import java.time.LocalDateTime

data class UsageHistoryUpdateRequest(
    val endAt: LocalDateTime?,
)
