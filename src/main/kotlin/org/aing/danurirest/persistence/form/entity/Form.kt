package org.aing.danurirest.persistence.form.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.company.entity.Company
import java.util.*

@Entity
class Form(
    @Id @GeneratedValue
    val id: UUID? = null,
    var title: String,
    @Lob
    var formSchema: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,
    @Column(nullable = false, name = "is_for_sign_up")
    var signUpForm: Boolean = false,
) : BaseEntity()
