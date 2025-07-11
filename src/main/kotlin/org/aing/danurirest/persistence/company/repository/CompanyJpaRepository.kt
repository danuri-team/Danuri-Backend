package org.aing.danurirest.persistence.company.repository

import org.aing.danurirest.persistence.company.entity.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CompanyJpaRepository : JpaRepository<Company, UUID>
