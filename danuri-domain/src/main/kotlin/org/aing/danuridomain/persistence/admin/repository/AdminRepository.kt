package org.aing.danuridomain.persistence.admin.repository

import org.aing.danuridomain.persistence.admin.entity.Admin
import java.util.Optional
import java.util.UUID

interface AdminRepository {
    fun findByID(adminId: UUID): Optional<Admin>

    fun findByEmail(email: String): Optional<Admin>

    fun existsByEmail(email: String): Boolean

    fun save(admin: Admin): Admin

    fun findByCompanyId(companyId: UUID): List<Admin>

    fun delete(adminId: UUID)
}
