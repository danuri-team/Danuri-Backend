package org.aing.danurirest.persistence.admin.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.admin.Status
import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.user.Role
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
