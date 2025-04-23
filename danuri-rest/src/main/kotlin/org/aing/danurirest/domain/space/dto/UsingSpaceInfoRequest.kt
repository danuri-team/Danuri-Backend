package org.aing.danurirest.domain.space.dto

import java.util.UUID

data class UsingSpaceInfoRequest(
    val usageId: UUID,
    val userId: UUID,
)
