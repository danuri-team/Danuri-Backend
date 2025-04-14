package org.aing.danuridomain.persistence.item.repository.impl

import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.admin.repository.AdminJpaRepository
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import java.util.*

class ItemRepositoryImpl(
    private val adminJpaRepository: AdminJpaRepository
): ItemRepository {
    override fun save(admin: Item): Item = adminJpaRepository.save(admin)
    override fun findById(id: UUID): Optional<Item> = adminJpaRepository.findById(id)
}