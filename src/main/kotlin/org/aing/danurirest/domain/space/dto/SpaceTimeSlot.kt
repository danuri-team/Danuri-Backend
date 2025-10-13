package org.aing.danurirest.domain.space.dto

import java.time.LocalTime

data class SpaceTimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isAvailable: Boolean,
)
