package org.aing.danurirest.persistence.help.repository

import org.aing.danurirest.persistence.help.entity.HelpHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HelpHistoryJpaRepository : JpaRepository<HelpHistory, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<HelpHistory>
}
