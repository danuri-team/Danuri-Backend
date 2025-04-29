package org.aing.danuridomain.persistence.item.repository

import org.aing.danuridomain.persistence.item.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ItemJpaRepository : JpaRepository<Item, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<Item>
}