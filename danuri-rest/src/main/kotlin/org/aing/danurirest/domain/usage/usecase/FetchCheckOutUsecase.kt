package org.aing.danurirest.domain.usage.usecase

import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FetchCheckOutUsecase(
    val usageHistoryRepository: UsageHistoryRepository,
    val userRepository: UserRepository,
) {
    fun execute() {
        val context = SecurityContextHolder.getContext().authentication.principal as ContextDto
        val user =
            userRepository.findById(context.id!!).orElseThrow {
                CustomException(CustomErrorCode.NOT_FOUND_USER)
            }
        usageHistoryRepository.updateEndDate(
            user = user,
            endDate = LocalDateTime.now(),
        )
    }
}
