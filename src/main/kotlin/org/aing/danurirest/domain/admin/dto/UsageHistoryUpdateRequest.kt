package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class UsageHistoryUpdateRequest(
    @field:NotNull(message = "종료 시간은 필수 입력값입니다.")
    val endAt: LocalDateTime,
)
