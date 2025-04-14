package org.aing.danuridomain.persistence.admin.repository.impl

import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.admin.repository.AdminJpaRepository
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import java.util.*

class AdminRepositoryImpl(
    private val adminJpaRepository: AdminJpaRepository
): AdminRepository {
    override fun save(admin: Item): Item = adminJpaRepository.save(admin)

    override fun findById(id: UUID): Optional<Item> = adminJpaRepository.findById(id)
}