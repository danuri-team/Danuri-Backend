package org.aing.danuridomain.persistence.device.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.aing.danuridomain.persistence.company.entity.Company
import org.springframework.data.annotation.CreatedDate
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
    @CreatedDate
    @Column(nullable = false)
    val create_at: LocalDateTime,
    @Column(nullable = true)
    val end_at: LocalDateTime? = null,
)
