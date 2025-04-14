package org.aing.danuridomain.persistence.usage.repository

import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UsageHistoryJpaRepository : JpaRepository<UsageHistory, UUID>
