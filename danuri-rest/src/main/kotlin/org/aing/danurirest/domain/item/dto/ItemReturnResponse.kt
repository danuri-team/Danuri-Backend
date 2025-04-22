package org.aing.danurirest.domain.item.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

data class ItemReturnResponse(
    val id: UUID?,
    @field:NotNull(message = "아이템 ID는 필수입니다.")
    val itemId: UUID,
    @field:NotNull(message = "아이템 이름은 필수입니다.")
    val itemName: String,
    @field:NotNull(message = "총 대여 수량은 필수입니다.")
    val totalQuantity: Int,
    @field:NotNull(message = "반납 수량은 필수입니다.")
    val returnedQuantity: Int,
    @field:NotNull(message = "반납 시간은 필수입니다.")
    val returnedAt: LocalDateTime
) 