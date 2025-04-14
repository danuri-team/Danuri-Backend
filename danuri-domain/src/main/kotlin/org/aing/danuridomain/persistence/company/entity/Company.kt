package org.aing.danuridomain.persistence.company.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.aing.danuridomain.persistence.admin.entity.Admin
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Company(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @OneToMany(mappedBy = "company")
    val admin: List<Admin>,

    @Column(nullable = false)
    val name: String,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime
)
