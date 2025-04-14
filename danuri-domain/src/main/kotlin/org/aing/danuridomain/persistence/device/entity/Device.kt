package org.aing.danuridomain.persistence.device.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.aing.danuridomain.persistence.company.entity.Company
import org.springframework.data.annotation.CreatedBy
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Device(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @CreatedBy
    @Column(nullable = false)
    val createdAt: LocalDateTime,
    @Column
    val endAt: LocalDateTime? = null,
)
