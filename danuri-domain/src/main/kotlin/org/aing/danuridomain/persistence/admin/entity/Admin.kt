package org.aing.danuridomain.persistence.admin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.aing.danuridomain.persistence.BaseEntity
import org.aing.danuridomain.persistence.admin.Status
import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.usage.entity.UsageHistory
import org.aing.danuridomain.persistence.user.Role
import java.util.UUID

@Entity
class Admin(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    val company: Company,
    @Column(nullable = false)
    var email: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    var phone: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.ROLE_ADMIN,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: Status = Status.NEED_COMPANY_APPROVE,
) : BaseEntity()
