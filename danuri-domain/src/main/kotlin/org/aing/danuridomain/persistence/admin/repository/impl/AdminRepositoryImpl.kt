package org.aing.danuridomain.persistence.admin.repository.impl

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.admin.repository.AdminJpaRepository
import org.aing.danuridomain.persistence.admin.repository.AdminRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class AdminRepositoryImpl(
    private val adminJpaRepository: AdminJpaRepository,
) : AdminRepository {
    override fun findByID(adminId: UUID): Optional<Admin> = adminJpaRepository.findById(adminId)

    override fun findByEmail(email: String): Optional<Admin> = adminJpaRepository.findByEmail(email)

    override fun existsByEmail(email: String): Boolean = adminJpaRepository.existsByEmail(email)

    override fun save(admin: Admin): Admin = adminJpaRepository.save(admin)
}
