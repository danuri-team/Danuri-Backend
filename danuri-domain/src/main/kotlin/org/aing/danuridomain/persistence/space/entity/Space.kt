package org.aing.danuridomain.persistence.space.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.util.UUID

@Entity
data class Space(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "usage_id")
    val usage: UsageHistory,
    @ManyToOne
    @JoinColumn(name = "company_id")
    val company: Company,
)
