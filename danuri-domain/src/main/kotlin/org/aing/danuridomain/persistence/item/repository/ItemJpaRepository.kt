package org.aing.danuridomain.persistence.item.repository

import org.aing.danuridomain.persistence.item.ItemStatus
import org.aing.danuridomain.persistence.item.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ItemJpaRepository : JpaRepository<Item, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<Item>

    @Suppress("ktlint:standard:function-naming")
    fun findByCompanyIdAndAvailableQuantityGreaterThanAndStatus(
        companyId: UUID,
        availableQuantity: Int = 0,
        status: ItemStatus = ItemStatus.AVAILABLE,
    ): List<Item>

    fun findByCompanyIdAndId(
        companyId: UUID,
        id: UUID,
    ): Item
}
