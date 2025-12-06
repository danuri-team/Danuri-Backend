package org.aing.danurirest.persistence.user.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.form.entity.FormResult
import org.aing.danurirest.persistence.usage.entity.UsageHistory
import java.util.UUID

@Entity(name = "users")
class User(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "company_id")
    val company: Company,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val usages: List<UsageHistory> = emptyList(),
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val signUpForm: FormResult? = null,
    @Column(nullable = false, length = 30)
    var phone: String,
) : BaseEntity()
