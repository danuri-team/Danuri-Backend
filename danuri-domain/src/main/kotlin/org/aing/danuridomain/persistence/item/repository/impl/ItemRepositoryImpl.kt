package org.aing.danuridomain.persistence.item.repository.impl

import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.item.repository.ItemJpaRepository
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class ItemRepositoryImpl(
    private val itemJpaRepository: ItemJpaRepository,
) : ItemRepository {
    override fun save(admin: Item): Item = itemJpaRepository.save(admin)

    override fun findById(id: UUID): Optional<Item> = itemJpaRepository.findById(id)
}
