package org.aing.danuridomain.persistence.admin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.aing.danuridomain.persistence.company.entity.Company
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Admin(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,

    @Column(nullable = false, length = 10)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val phone: String,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime,
)
