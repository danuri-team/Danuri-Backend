package org.aing.danuridomain.persistence.item.entity

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
import org.aing.danuridomain.persistence.BaseEntity
import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.item.ItemStatus
import org.aing.danuridomain.persistence.rental.entity.Rental
import java.util.UUID

@Entity
data class Item(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @Column(nullable = false, length = 10)
    val name: String,
    @Column(nullable = false)
    val totalQuantity: Int,
    @Column(nullable = false)
    var availableQuantity: Int,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ItemStatus,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @OneToMany(mappedBy = "item", cascade = [CascadeType.ALL])
    val rentals: List<Rental> = emptyList(),
) : BaseEntity()
