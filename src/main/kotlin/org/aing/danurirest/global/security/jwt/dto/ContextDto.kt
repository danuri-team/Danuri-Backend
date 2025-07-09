package org.aing.danurirest.global.security.jwt.dto

import org.aing.danurirest.persistence.admin.entity.Admin
import org.aing.danurirest.persistence.device.entity.Device
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.entity.User
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import java.util.UUID

data class ContextDto(
    val id: UUID? = null,
    val role: Role,
) {
    companion object {
        fun from(entity: Any): ContextDto =
            when (entity) {
                is Admin ->
                    ContextDto(
                        id = entity.id,
                        role = entity.role,
                    )
                is Device ->
                    ContextDto(
                        id = entity.id,
                        role = entity.role,
                    )
                is User ->
                    ContextDto(
                        id = entity.id,
                        role = Role.ROLE_USER,
                    )
                else -> throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
            }
    }
}
