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
import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.user.enum.Age
import org.aing.danuridomain.persistence.user.enum.Sex
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
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
    @CreatedDate
    @Column(nullable = false, updatable = false)
    val create_at: LocalDateTime,
    @UpdateTimestamp
    @Column(nullable = false)
    val update_at: LocalDateTime,
)
