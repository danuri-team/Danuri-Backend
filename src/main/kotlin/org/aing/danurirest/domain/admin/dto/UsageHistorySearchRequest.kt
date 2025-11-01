package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

data class UsageHistorySearchRequest(
    val spaceId: UUID? = null,
    val userId: UUID? = null,
    @field:NotNull(message = "시작 시간은 필수입니다.")
    val startDate: LocalDateTime,
    val endDate: LocalDateTime? = null,
)
