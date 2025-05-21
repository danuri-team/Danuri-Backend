package org.aing.danurirest.global.util

import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class GetCurrentUser {
    companion object {
        fun getUser(): ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
    }
}
