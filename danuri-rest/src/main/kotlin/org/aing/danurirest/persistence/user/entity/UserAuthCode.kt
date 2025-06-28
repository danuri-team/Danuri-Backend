package org.aing.danurirest.persistence.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
class UserAuthCode(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    
    @Column(nullable = false, length = 20)
    val phone: String,
    
    @Column(nullable = false, length = 6)
    val authCode: String,
    
    @Column(nullable = false)
    val expiredAt: LocalDateTime,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) 