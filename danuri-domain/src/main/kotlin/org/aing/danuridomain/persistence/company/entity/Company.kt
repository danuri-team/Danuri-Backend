package org.aing.danuridomain.persistence.company.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.aing.danuridomain.persistence.device.entity.Device
import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.user.entity.User
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Company(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val item: List<Item> = emptyList(),
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val user: List<User> = emptyList(),
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val device: List<Device> = emptyList(),
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val spaces: List<Space> = emptyList(),
    @Column(nullable = false)
    val name: String,
    @CreatedDate
    @Column(nullable = false, updatable = false)
    val create_at: LocalDateTime,
)
