package org.aing.danurirest.domain.item.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.UUID

data class ItemRentalRequest(
    @field:NotNull(message = "아이템 ID는 필수입니다.")
    val itemId: UUID,
    @field:NotNull(message = "대여 수량은 필수입니다.")
    @field:Positive(message = "대여 수량은 1 이상이어야 합니다.")
    val quantity: Int,
    @field:NotNull(message = "이용 ID는 필수입니다.")
    val usageId: UUID,
)
