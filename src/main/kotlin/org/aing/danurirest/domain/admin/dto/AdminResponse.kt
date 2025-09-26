package org.aing.danurirest.domain.admin.dto

import org.aing.danurirest.persistence.admin.Status
import org.aing.danurirest.persistence.admin.entity.Admin
import java.time.LocalDateTime
import java.util.UUID

data class AdminResponse(
    val id: UUID,
    val email: String,
    val phone: String,
    val status: Status,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(entity: Admin): AdminResponse =
            AdminResponse(
                id = entity.id!!,
                email = entity.email,
                phone = entity.phone,
                status = entity.status,
                createdAt = entity.createdAt!!,
            )
    }
}
