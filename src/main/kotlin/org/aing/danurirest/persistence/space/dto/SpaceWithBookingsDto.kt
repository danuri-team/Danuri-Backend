package org.aing.danurirest.persistence.space.dto

import org.aing.danurirest.persistence.space.entity.Space

data class SpaceWithBookingsDto(
    val space: Space,
    val bookedRanges: List<BookedTimeRange>,
)
