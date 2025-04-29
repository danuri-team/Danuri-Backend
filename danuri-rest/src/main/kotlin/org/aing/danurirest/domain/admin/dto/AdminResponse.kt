package org.aing.danurirest.domain.admin.dto

import org.aing.danuridomain.persistence.admin.Status
import org.aing.danuridomain.persistence.admin.entity.Admin
import org.aing.danuridomain.persistence.user.enum.Role
import java.util.UUID

data class AdminResponse(
    val id: UUID,
    val companyId: UUID,
    val companyName: String,
    val email: String,
    val phone: String,
    val role: Role,
    val status: Status
) {
    companion object {
        fun from(entity: Admin): AdminResponse =
            AdminResponse(
                id = entity.id!!,
                companyId = entity.company.id!!,
                companyName = entity.company.name,
                email = entity.email,
                phone = entity.phone,
                role = entity.role,
                status = entity.status
            )
    }
} 