package org.aing.danuridomain.persistence.admin.repository

import org.aing.danuridomain.persistence.admin.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdminJpaRepository : JpaRepository<Admin, UUID>
