package org.aing.danurirest.persistence.user.repository

import org.aing.danurirest.persistence.user.entity.UserAuthCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserAuthCodeJpaRepository : JpaRepository<UserAuthCode, UUID> {
    fun findByPhone(phone: String): List<UserAuthCode>

    fun deleteByPhone(phone: String)
}
