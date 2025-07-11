package org.aing.danurirest.persistence.company.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.item.entity.Item
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.user.entity.User
import java.util.UUID

@Entity
class Company(
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
