package org.aing.danuridomain.persistence.company.repository

import org.aing.danuridomain.persistence.company.entity.Company
import java.util.Optional
import java.util.UUID

interface CompanyRepository {
    fun findById(id: UUID) : Optional<Company>
}