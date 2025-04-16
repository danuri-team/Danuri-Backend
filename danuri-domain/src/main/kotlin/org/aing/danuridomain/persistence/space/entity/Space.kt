package org.aing.danuridomain.persistence.space.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalTime
import java.util.UUID

@Entity
data class Space(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @OneToMany(mappedBy = "space")
    val usage: List<UsageHistory> = emptyList(),
    @ManyToOne
    @JoinColumn(name = "company_id")
    val company: Company,
    @Column(nullable = false)
    val startAt: LocalTime,
    @Column(nullable = false)
    val endAt: LocalTime,
)
