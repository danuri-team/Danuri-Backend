package org.aing.danurirest.persistence.item.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.rental.entity.Rental
import java.util.UUID

@Entity
class Item(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @Column(nullable = false, length = 10)
    var name: String,
    @Column(nullable = false)
    var totalQuantity: Int,
    @Column(nullable = false)
    var availableQuantity: Int,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ItemStatus,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @OneToMany(mappedBy = "item", cascade = [CascadeType.ALL], orphanRemoval = true)
    val rentals: MutableList<Rental> = mutableListOf(),
) : BaseEntity()
