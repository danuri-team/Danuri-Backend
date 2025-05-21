package org.aing.danurirest.domain.usage.dto

import java.time.LocalDateTime
import java.util.UUID

data class FetchCheckOutRequest(
    val usageId: UUID,
    val endAt: LocalDateTime?,
)
