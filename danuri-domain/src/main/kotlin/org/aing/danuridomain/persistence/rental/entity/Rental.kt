package org.aing.danuridomain.persistence.rental.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.aing.danuridomain.persistence.BaseEntity
import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Rental(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "item_id")
    val item: Item,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usage_id")
    val usage: UsageHistory,
    @Column(nullable = false)
    val quantity: Int,
    @Column(nullable = false)
    val borrowedAt: LocalDateTime,
    @Column(nullable = true)
    var returnedAt: LocalDateTime? = null,
    @Column(nullable = false)
    var returnedQuantity: Int = 0,
) : BaseEntity()
