package org.aing.danurirest.domain.usage.usecase

import org.aing.danuridomain.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class FetchCheckOutUsecase(
    val usageHistoryRepository: UsageHistoryRepository,
) {
    fun execute() {
        val context = SecurityContextHolder.getContext().authentication.principal as ContextDto
        val result =
            usageHistoryRepository
                .findAllByUserIdAndDateRange(
                    userId = context.id!!,
                    currentTime = LocalDateTime.now(),
                )

        if (result.isEmpty()) {
            throw CustomException(CustomErrorCode.NOT_FOUND)
        }

        result[0].endAt = LocalDateTime.now()
    }
}
