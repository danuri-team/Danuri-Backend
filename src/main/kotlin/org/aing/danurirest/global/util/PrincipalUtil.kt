package org.aing.danurirest.global.util

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

object PrincipalUtil {
    fun getContextDto(): ContextDto =
        SecurityContextHolder.getContext().authentication.principal as? ContextDto
            ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)

    fun getUserId(): UUID =
        getContextDto().id
            ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
}
