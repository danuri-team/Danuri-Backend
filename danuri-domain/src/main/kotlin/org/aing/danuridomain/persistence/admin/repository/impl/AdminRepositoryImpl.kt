package org.aing.danuridomain.persistence.admin.repository.impl

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminJpaRepository
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AdminRepositoryImpl(
    private val adminJpaRepository: AdminJpaRepository,
) : AdminRepository {
    override fun save(admin: Admin): Admin = adminJpaRepository.save(admin)

    override fun findById(id: UUID): Optional<Admin> = adminJpaRepository.findById(id)
}
