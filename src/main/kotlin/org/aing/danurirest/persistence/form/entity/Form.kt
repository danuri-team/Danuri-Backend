package org.aing.danurirest.persistence.form.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.company.entity.Company
import java.util.*

@Entity
class Form(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID? = null,
    var title: String,
    @Lob
    var schema: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,
) : BaseEntity()
