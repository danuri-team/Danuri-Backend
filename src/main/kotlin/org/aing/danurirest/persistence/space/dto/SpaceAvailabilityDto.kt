package org.aing.danurirest.persistence.space.dto

import org.aing.danurirest.persistence.space.entity.Space

data class SpaceAvailabilityDto(
    val space: Space,
    val isAvailable: Boolean,
)
