package org.aing.danurirest.domain.auth.dto

import org.aing.danuridomain.persistence.admin.entity.Admin

data class AdminInfoResponse(
    val companyName: String,
    val email: String,
    val phone: String,
) {
    companion object {
        fun from(entity: Admin): AdminInfoResponse =
            AdminInfoResponse(
                companyName = entity.company.name,
                email = entity.email,
                phone = entity.phone,
            )
    }
}
