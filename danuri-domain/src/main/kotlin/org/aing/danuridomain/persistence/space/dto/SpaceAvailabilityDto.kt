package org.aing.danuridomain.persistence.space.dto

import org.aing.danuridomain.persistence.space.entity.Space

data class SpaceAvailabilityDto(
    val space: Space,
    val isAvailable: Boolean,
)
