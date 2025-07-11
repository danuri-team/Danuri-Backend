package org.aing.danurirest.persistence.user.repository

import org.aing.danurirest.persistence.user.entity.UserAuthCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

interface UserAuthCodeJpaRepository : JpaRepository<UserAuthCode, UUID> {
    fun findByPhone(phone: String): Optional<UserAuthCode>

    @Transactional
    @Modifying
    @Query("DELETE FROM UserAuthCode u WHERE u.phone = :phone")
    fun deleteByPhone(
        @Param("phone") phone: String,
    )
} 
