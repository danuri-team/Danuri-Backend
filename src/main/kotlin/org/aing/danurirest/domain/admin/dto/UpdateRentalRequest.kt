package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.aing.danurirest.persistence.rental.RentalStatus

data class UpdateRentalRequest(
    @field:Min(value = 1, message = "총 수량은 최소 1개 이상이어야 합니다.")
    val quantity: Int,
    @field:Min(value = 0, message = "반납 수량은 0개 이상이어야 합니다.")
    val returnedQuantity: Int,
    @field:NotNull(message = "상태는 필수 값입니다.")
    val status: RentalStatus,
)
