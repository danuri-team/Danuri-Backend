package org.aing.danurirest.domain.admin.dto

import java.time.LocalDateTime
import java.util.UUID

data class UsageHistorySearchRequest(
    val companyId: UUID? = null,
    val spaceId: UUID? = null,
    val userId: UUID? = null,
    val startDate: LocalDateTime = LocalDateTime.now().minusMonths(1),
    val endDate: LocalDateTime = LocalDateTime.now(),
) 