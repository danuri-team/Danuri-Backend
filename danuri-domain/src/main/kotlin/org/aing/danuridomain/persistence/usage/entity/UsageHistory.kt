package org.aing.danuridomain.persistence.usage.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class UsageHistory(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToOne(mappedBy = "usage")
    @JoinColumn(name = "space_id")
    val space: Space,
    @Column(name = "start_at")
    val startAt: LocalDateTime,
    @Column(name = "end_at")
    val endAt: LocalDateTime? = null,
)
