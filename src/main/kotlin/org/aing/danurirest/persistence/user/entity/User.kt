package org.aing.danurirest.persistence.user.entity

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
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import java.util.UUID

@Entity
class User(
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
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var sex: Sex,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var age: Age,
    @Column(nullable = false, length = 30)
    var phone: String,
) : BaseEntity()
