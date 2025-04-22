package org.aing.danurirest.domain.space.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

data class SpaceUsageResponse(
    val id: UUID?,
    @field:NotNull(message = "사용 시작 시간은 필수입니다.")
    @field:PastOrPresent(message = "사용 시작 시간은 현재 또는 과거여야 합니다.")
    val startAt: LocalDateTime,
    val endAt: LocalDateTime? = null,
) {
    companion object {
        fun from(entity: UsageHistory): SpaceUsageResponse =
            SpaceUsageResponse(
                id = entity.id,
                startAt = entity.start_at,
                endAt = entity.end_at,
            )
    }
}
