package org.aing.danuridomain.persistence.user.repository.impl

import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.repository.UserJpaRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findById(userId: UUID): Optional<User> = userJpaRepository.findById(userId)

    override fun save(user: User): User = userJpaRepository.save(user)
    
    override fun findByCompanyId(companyId: UUID): List<User> = userJpaRepository.findAllByCompanyId(companyId)
    
    override fun findByPhoneAndCompanyId(phone: String, companyId: UUID): Optional<User> = 
        userJpaRepository.findByPhoneAndCompanyId(phone, companyId)
    
    override fun delete(userId: UUID) {
        userJpaRepository.deleteById(userId)
    }
    
    override fun update(user: User): User = userJpaRepository.save(user)
}
