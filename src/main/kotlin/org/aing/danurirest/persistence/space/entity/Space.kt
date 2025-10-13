package org.aing.danurirest.persistence.space.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import java.time.LocalTime
import java.util.UUID

@Entity
class Space(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val usage: List<UsageHistory> = emptyList(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @Column(nullable = false, length = 50)
    var name: String,
    @Column(nullable = false)
    val allowOverlap: Boolean = false,
    @Column(nullable = false)
    var startAt: LocalTime,
    @Column(nullable = false)
    var endAt: LocalTime,
) : BaseEntity()
