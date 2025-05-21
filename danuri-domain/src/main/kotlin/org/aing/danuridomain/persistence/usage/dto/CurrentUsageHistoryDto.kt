package org.aing.danuridomain.persistence.usage.dto

data class CurrentUsageHistoryDto(
    val isUsingSpace: Boolean,
    val spaceUsageInfo: DetailUsageInfo,
)
