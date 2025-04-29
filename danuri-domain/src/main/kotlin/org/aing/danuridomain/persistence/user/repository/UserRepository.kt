package org.aing.danuridomain.persistence.user.repository

import org.aing.danuridomain.persistence.user.entity.User
import java.util.Optional
import java.util.UUID

interface UserRepository {
    fun findById(userId: UUID): Optional<User>
    
    fun save(user: User): User
    
    fun findByCompanyId(companyId: UUID): List<User>
    
    fun findByPhoneAndCompanyId(phone: String, companyId: UUID): Optional<User>
    
    fun delete(userId: UUID)
    
    fun update(user: User): User
}
