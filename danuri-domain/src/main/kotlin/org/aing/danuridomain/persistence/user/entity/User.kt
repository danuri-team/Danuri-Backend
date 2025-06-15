package org.aing.danuridomain.persistence.user.entity

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
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.user.Age
import org.aing.danuridomain.persistence.user.Sex
import java.util.UUID

@Entity
data class User(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "company_id")
    val company: Company,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @Column(nullable = false)
    val usages: List<UsageHistory> = emptyList(),
    @Column(nullable = false, length = 20)
    val name: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val sex: Sex,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val age: Age,
    @Column(nullable = false, length = 30)
    val phone: String,
) : BaseEntity()
