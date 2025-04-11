package org.aing.danuridomain.persistence.company.repository

import org.aing.danuridomain.persistence.company.entity.Company
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CompanyJpaRepository: JpaRepository<Company, UUID>