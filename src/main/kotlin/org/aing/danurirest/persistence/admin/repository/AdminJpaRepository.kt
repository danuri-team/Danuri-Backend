package org.aing.danurirest.persistence.admin.repository

import org.aing.danurirest.persistence.admin.entity.Admin
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface AdminJpaRepository : JpaRepository<Admin, UUID> {
    fun findByEmail(email: String): Optional<Admin>

    fun existsByEmail(email: String): Boolean

    fun findAllByCompanyId(
        companyId: UUID,
        pageable: Pageable,
    ): Page<Admin>

    fun findByPhone(phone: String): Optional<Admin>

    fun findByIdAndCompanyId(
        id: UUID,
        companyId: UUID,
    ): Admin?
}
