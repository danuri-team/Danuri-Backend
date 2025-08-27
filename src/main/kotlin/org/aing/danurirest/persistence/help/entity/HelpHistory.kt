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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,
    @ManyToOne
    @JoinColumn(name = "checked_admin_id")
    var checkedAdmin: Admin? = null,
    @Column
    var isResolved: Boolean = false,
) : BaseEntity()
