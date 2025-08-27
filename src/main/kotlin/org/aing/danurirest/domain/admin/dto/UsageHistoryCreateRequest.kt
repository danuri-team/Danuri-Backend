package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

data class UsageHistoryCreateRequest(
    @field:NotNull(message = "사용자 ID는 필수 입력값입니다.")
    val userId: UUID,
    @field:NotNull(message = "공간 ID는 필수 입력값입니다.")
    val spaceId: UUID,
    @field:NotNull(message = "시작 시간은 필수 입력값입니다.")
    val startAt: LocalDateTime,
    @field:NotNull(message = "종료 시간은 필수 입력값입니다.")
    val endAt: LocalDateTime,
)
