package org.aing.danurirest.persistence.usage.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import java.util.UUID

@Entity
class AdditionalParticipant(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usage_history_id", nullable = false)
    val usageHistory: UsageHistory,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val sex: Sex,
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false)
    val ageGroup: Age,
    @Column(nullable = false)
    val count: Int,
    @Column(name = "max_count", nullable = false)
    val maxCount: Int,
) : BaseEntity()
