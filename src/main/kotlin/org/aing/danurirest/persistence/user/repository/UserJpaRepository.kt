package org.aing.danurirest.persistence.user.repository

import org.aing.danurirest.persistence.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<User, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<User>

    fun findByPhone(phone: String): Optional<User>

    fun existsByPhone(phone: String): Boolean

    fun findByPhoneAndCompanyId(
        phone: String,
        companyId: UUID,
    ): User?

    fun findByIdAndCompanyId(id: UUID, companyId: UUID): User?
}
