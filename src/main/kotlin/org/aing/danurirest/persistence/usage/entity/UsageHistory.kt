package org.aing.danurirest.persistence.usage.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.aing.danurirest.persistence.BaseEntity
import org.aing.danurirest.persistence.rental.entity.Rental
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

@Entity
class UsageHistory(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    val space: Space,
    @OneToMany(mappedBy = "usage", cascade = [CascadeType.ALL])
    val rental: List<Rental> = emptyList(),
    @Column(name = "start_at", nullable = false)
    val startAt: LocalDateTime,
    @Column(name = "end_at", nullable = true)
    var endAt: LocalDateTime?,
) : BaseEntity()
