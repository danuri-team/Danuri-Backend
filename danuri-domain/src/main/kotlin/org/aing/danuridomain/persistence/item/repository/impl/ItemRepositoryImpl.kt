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
    override fun findById(itemId: UUID): Optional<Item> = itemJpaRepository.findById(itemId)

    override fun save(item: Item): Item = itemJpaRepository.save(item)
    
    override fun findByCompanyId(companyId: UUID): List<Item> = itemJpaRepository.findAllByCompanyId(companyId)
    
    override fun delete(itemId: UUID) {
        itemJpaRepository.deleteById(itemId)
    }
    
    override fun update(item: Item): Item = itemJpaRepository.save(item)
}
