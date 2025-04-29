package org.aing.danuridomain.persistence.user.repository

import org.aing.danuridomain.persistence.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserJpaRepository : JpaRepository<User, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<User>
    
    fun findByPhoneAndCompanyId(phone: String, companyId: UUID): Optional<User>
}
