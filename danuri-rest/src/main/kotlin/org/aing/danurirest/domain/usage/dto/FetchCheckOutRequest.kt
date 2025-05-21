package org.aing.danurirest.domain.usage.dto

import java.time.LocalDateTime
import java.util.*

data class FetchCheckOutRequest(
    val usageId: UUID,
    val endAt: LocalDateTime,
)
