package org.aing.danuridomain.persistence.company.repository.impl

import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.company.repository.CompanyJpaRepository
import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CompanyRepositoryImpl(
    private val companyJpaRepository: CompanyJpaRepository
): CompanyRepository {
    override fun findById(id: UUID): Optional<Company> = companyJpaRepository.findById(id)

    override fun save(company: Company): Company = companyJpaRepository.save(company)
}