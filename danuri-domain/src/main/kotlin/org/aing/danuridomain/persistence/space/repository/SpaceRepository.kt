package org.aing.danuridomain.persistence.space.repository

import org.aing.danuridomain.persistence.space.entity.Space
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

interface SpaceRepository {
    fun getAvailableSpace(
        companyId: UUID,
        targetTime: LocalTime = LocalTime.now(),
        targetDateTime: LocalDateTime = LocalDateTime.now(),
    ): List<Space>
}
