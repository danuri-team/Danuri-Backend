package org.aing.danuridomain.persistence.item.repository

import org.aing.danuridomain.persistence.item.entity.Item
import java.util.Optional
import java.util.UUID

interface ItemRepository {
    fun save(admin: Item): Item
    fun findById(id: UUID): Optional<Item>
}