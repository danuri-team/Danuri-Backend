package org.aing.danurirest.domain.admin.dto

import org.aing.danurirest.persistence.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val phone: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val usageCount: Int,
) {
    companion object {
        fun from(entity: User): UserResponse =
            UserResponse(
                id = entity.id!!,
                phone = entity.phone,
                createdAt = entity.createdAt!!,
                updatedAt = entity.updatedAt,
                usageCount = entity.usages.size,
            )
    }
}
