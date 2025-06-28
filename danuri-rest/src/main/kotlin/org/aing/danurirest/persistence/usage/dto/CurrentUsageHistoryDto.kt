package org.aing.danurirest.persistence.usage.dto

data class CurrentUsageHistoryDto(
    val isUsingSpace: Boolean,
    val spaceUsageInfo: DetailUsageInfo,
)
