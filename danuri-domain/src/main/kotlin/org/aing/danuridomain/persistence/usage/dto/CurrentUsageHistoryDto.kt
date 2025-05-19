package org.aing.danuridomain.persistence.usage.dto

import java.util.UUID

data class CurrentUsageHistoryDto(
    val userId: UUID,
    val isUsingSpace: Boolean,
    val spaceUsageInfo: DetailUsageInfo,
)
