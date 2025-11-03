package org.aing.danurirest.persistence.usage.repository

import org.aing.danurirest.persistence.usage.dto.CurrentUsageHistoryDto
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
        pageable: Pageable,
    ): Page<UsageHistory>

    fun findAllByCompanyIdAndSpaceIdAndDateRange(
        spaceId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
        pageable: Pageable,
    ): Page<UsageHistory>

    fun findAllByUserIdAndDateRangeAndCompanyId(
        userId: UUID,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        companyId: UUID,
        pageable: Pageable,
    ): Page<UsageHistory>

    fun findUserCurrentUsageInfo(userId: UUID): CurrentUsageHistoryDto
}
