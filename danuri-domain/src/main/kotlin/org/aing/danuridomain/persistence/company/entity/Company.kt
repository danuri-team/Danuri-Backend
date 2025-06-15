package org.aing.danuridomain.persistence.company.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.aing.danuridomain.persistence.BaseEntity
import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.device.entity.Device
import org.aing.danuridomain.persistence.item.entity.Item
import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.user.entity.User
import java.util.UUID

@Entity
data class Company(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @Column(nullable = false)
    val name: String,
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val item: List<Item> = emptyList(),
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val user: List<User> = emptyList(),
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val admin: List<Admin> = emptyList(),
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val device: List<Device> = emptyList(),
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val spaces: List<Space> = emptyList(),
) : BaseEntity()
