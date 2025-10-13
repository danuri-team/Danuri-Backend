package org.aing.danurirest.persistence.space.dto

data class BookedTimeRange(
    val startTime: java.time.LocalDateTime,
    val endTime: java.time.LocalDateTime,
)
