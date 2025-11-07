package org.aing.danurirest.domain.usage.dto

import java.time.LocalTime
import java.util.*

data class CreateSpaceUsageRequest(
    val spaceId: UUID,
    val startAt: LocalTime,
    val additionalParticipants: List<AdditionalParticipantsDto>,
)
