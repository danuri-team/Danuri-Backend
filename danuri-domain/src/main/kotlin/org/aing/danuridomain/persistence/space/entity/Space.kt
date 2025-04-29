package org.aing.danuridomain.persistence.space.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
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
    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY)
    val usage: List<UsageHistory> = emptyList(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @Column(nullable = false, length = 50)
    val name: String,
    @Column(nullable = false)
    val start_at: LocalTime,
    @Column(nullable = false)
    val end_at: LocalTime,
)
