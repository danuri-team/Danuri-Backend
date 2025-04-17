package org.aing.danuridomain.persistence.admin.repository

import org.aing.danuridomain.persistence.admin.entity.Admin
import java.util.Optional
import java.util.UUID

interface AdminRepository {
    fun save(admin: Admin): Admin

    fun findById(id: UUID): Optional<Admin>
}
