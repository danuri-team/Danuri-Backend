package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateRentalRequest(
    @field:NotNull(message = "아이템 ID는 필수 입력값입니다.")
    val itemId: UUID,
    @field:Min(value = 1, message = "총 수량은 최소 1개 이상이어야 합니다.")
    val quantity: Int,
    @field:NotNull(message = "유저 ID는 필수 입력값입니다.")
    val usageId: UUID,
)
