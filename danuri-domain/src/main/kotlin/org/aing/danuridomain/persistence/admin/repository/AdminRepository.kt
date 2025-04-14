package org.aing.danuridomain.persistence.admin.repository

import org.aing.danuridomain.persistence.item.entity.Item
import java.util.Optional
import java.util.UUID

interface AdminRepository {
    fun save(admin: Item): Item
    fun findById(id: UUID): Optional<Item>
}