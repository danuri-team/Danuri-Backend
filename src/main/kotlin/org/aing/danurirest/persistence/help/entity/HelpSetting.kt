package org.aing.danurirest.persistence.help.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.company.entity.Company
import java.util.UUID

@Entity
class HelpSetting(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @Column
    val enable: Boolean = false,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @OneToMany(mappedBy = "helpSetting", cascade = [CascadeType.ALL], orphanRemoval = true)
    val targetAdmins: List<Admin>,
) : BaseEntity()
