package org.aing.danurirest.persistence.usage.repository

import org.aing.danurirest.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Repository
interface UsageHistoryRepository {
    fun findByIdAndCompanyId(
        usageId: UUID,
        companyId: UUID,
    ): Optional<UsageHistory>

    fun findAllByCompanyIdAndDateRange(
        companyId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
    ): List<UsageHistory>

    fun findAllByCompanyIdAndSpaceIdAndDateRange(
        spaceId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
    ): List<UsageHistory>

    fun findAllByUserIdAndDateRangeAndCompanyId(
        userId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
    ): MutableList<UsageHistory>?

    fun findUserCurrentUsageInfo(userId: UUID): CurrentUsageHistoryDto
}
