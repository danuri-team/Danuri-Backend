package org.aing.danuridomain.persistence.company.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Company(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime
)
