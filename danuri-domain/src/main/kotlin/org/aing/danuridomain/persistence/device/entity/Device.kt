package org.aing.danuridomain.persistence.device.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.aing.danuridomain.persistence.company.entity.Company
import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.user.enum.Role
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Device(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.ROLE_DEVICE,
    @OneToOne
    @JoinColumn(name = "device_id")
    val space: Space,
    @CreatedDate
    @Column(nullable = false)
    val create_at: LocalDateTime = LocalDateTime.now(),
)
