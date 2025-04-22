package org.aing.danurirest.domain.item.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class ItemReturnRequest(
    @field:NotNull(message = "반납 수량은 필수입니다.")
    @field:Positive(message = "반납 수량은 1 이상이어야 합니다.")
    val quantity: Int
) 