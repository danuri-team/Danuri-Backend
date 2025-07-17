package org.aing.danurirest.domain.usage.usecase

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class FetchCheckOutUsecase(
    private val usageHistoryJpaRepository: UsageHistoryJpaRepository,
) {
    fun execute() {
        val context = PrincipalUtil.getContextDto()
        val result =
            usageHistoryJpaRepository
                .findCurrentUsageByUserId(
                    userId = context.id!!,
                    currentTime = LocalDateTime.now(),
                )

        if (result.isEmpty()) {
            throw CustomException(CustomErrorCode.NOT_FOUND)
        }

        result[0].endAt = LocalDateTime.now()
    }
}
