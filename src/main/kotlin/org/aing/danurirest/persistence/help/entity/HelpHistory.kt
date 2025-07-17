package org.aing.danurirest.persistence.help.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.company.entity.Company
import java.util.UUID

@Entity
class HelpHistory(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_admin_id")
    val checkedAdmin: Admin? = null,
    @Column
    val isResolved: Boolean = false,
) : BaseEntity()
