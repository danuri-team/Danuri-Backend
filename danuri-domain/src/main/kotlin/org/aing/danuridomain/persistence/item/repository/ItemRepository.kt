package org.aing.danuridomain.persistence.item.repository

import org.aing.danuridomain.persistence.item.entity.Item
import java.util.Optional
import java.util.UUID

interface ItemRepository {
    fun findById(itemId: UUID): Optional<Item>

    fun save(item: Item): Item

    fun findByCompanyId(companyId: UUID): List<Item>

    fun delete(itemId: UUID)

    fun update(item: Item): Item

    fun findByCompanyIdAndAvailableQuantityGreaterThanAndStatus(companyId: UUID): List<Item>
}
