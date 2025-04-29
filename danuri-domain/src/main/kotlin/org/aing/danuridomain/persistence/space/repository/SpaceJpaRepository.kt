package org.aing.danuridomain.persistence.space.repository

import org.aing.danuridomain.persistence.space.entity.Space
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpaceJpaRepository : JpaRepository<Space, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<Space>
}
