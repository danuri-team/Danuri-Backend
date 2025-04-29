package org.aing.danurirest.domain.auth.user.dto

import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.enum.Age
import org.aing.danuridomain.persistence.user.enum.Sex
import java.util.UUID

data class UserRegisterResponse(
    val id: UUID?,
    val name: String,
    val phone: String,
    val sex: Sex,
    val age: Age,
    val companyId: UUID
) {
    companion object {
        fun from(user: User): UserRegisterResponse =
            UserRegisterResponse(
                id = user.id,
                name = user.name,
                phone = user.phone,
                sex = user.sex,
                age = user.age,
                companyId = user.company.id!!
            )
    }
} 