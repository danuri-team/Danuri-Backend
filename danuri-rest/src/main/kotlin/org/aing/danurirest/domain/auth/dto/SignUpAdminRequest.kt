package org.aing.danurirest.domain.auth.dto

import org.aing.danuridomain.persistence.user.enum.Age
import org.aing.danuridomain.persistence.user.enum.Role
import org.aing.danuridomain.persistence.user.enum.Sex
import java.util.UUID

data class SignUpAdminRequest(
    val companyId: UUID,
    val name: String,
    val sex: Sex,
    val age: Age,
    val phone: String,
    val role: Role = Role.ROLE_ADMIN,
    val email: String,
    val password: String,
)
