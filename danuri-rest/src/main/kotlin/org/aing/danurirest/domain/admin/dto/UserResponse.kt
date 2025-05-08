package org.aing.danurirest.domain.admin.dto

import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.enum.Age
import org.aing.danuridomain.persistence.user.enum.Sex
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val companyId: UUID,
    val companyName: String,
    val name: String,
    val sex: Sex,
    val age: Age,
    val phone: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val usageCount: Int,
) {
    companion object {
        fun from(entity: User): UserResponse =
            UserResponse(
                id = entity.id!!,
                companyId = entity.company.id!!,
                companyName = entity.company.name,
                name = entity.name,
                sex = entity.sex,
                age = entity.age,
                phone = entity.phone,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                usageCount = entity.usages.size,
            )
    }
}
