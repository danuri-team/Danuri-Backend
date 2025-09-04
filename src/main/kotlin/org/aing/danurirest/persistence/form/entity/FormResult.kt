package org.aing.danurirest.persistence.form.entity

import jakarta.persistence.*
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.user.entity.User
import java.util.UUID

@Entity
class FormResult(
    @Id @GeneratedValue
    val id: UUID? = null,
    @Lob
    var result: String,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,
    @Column(nullable = false)
    val isSignUpResult: Boolean,
) : BaseEntity()
