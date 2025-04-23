package org.aing.danurirest.domain.space.dto

import java.util.UUID

data class UseSpaceRequest(
    val userId: UUID,
    val spaceId: UUID,
)
