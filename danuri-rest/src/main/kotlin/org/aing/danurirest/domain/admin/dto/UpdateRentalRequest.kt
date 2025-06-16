package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.Min

data class UpdateRentalRequest(
    @field:Min(value = 1, message = "총 수량은 최소 1개 이상이어야 합니다.")
    val quantity: Int,
    @field:Min(value = 1, message = "반납 수량은 최소 1개 이상이어야 합니다.")
    val returnedQuantity: Int,
)
