package org.aing.danurirest.persistence.rental.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.item.entity.Item
import org.aing.danurirest.persistence.rental.RentalStatus
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import java.time.LocalDateTime
import java.util.UUID

@Entity
class Rental(
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
    var quantity: Int,
    @Column(nullable = false)
    val borrowedAt: LocalDateTime,
    @Column(nullable = true)
    var returnedAt: LocalDateTime? = null,
    @Column(nullable = false)
    var returnedQuantity: Int = 0,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: RentalStatus = RentalStatus.NOT_CONFIRMED,
) : BaseEntity()
