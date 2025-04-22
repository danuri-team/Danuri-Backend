package org.aing.danurirest.domain.space.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import org.aing.danuridomain.persistence.rental.entity.Rental
import java.time.LocalDateTime
import java.util.UUID

data class RentalSimpleResponse(
    val id: UUID?,
    @field:NotNull(message = "아이템 ID는 필수입니다.")
    val itemId: UUID?,
    @field:NotNull(message = "아이템 이름은 필수입니다.")
    val name: String,
    @field:NotNull(message = "대여 시작 시간은 필수입니다.")
    @field:PastOrPresent(message = "대여 시작 시간은 현재 또는 과거여야 합니다.")
    val borrowedAt: LocalDateTime,
    val returnedAt: LocalDateTime?,
    @field:NotNull(message = "반납 여부는 필수입니다.")
    val isReturned: Boolean,
) {
    companion object {
        fun from(rental: Rental): RentalSimpleResponse =
            RentalSimpleResponse(
                id = rental.id,
                itemId = rental.item.id,
                name = rental.item.name,
                borrowedAt = rental.borrowed_at,
                returnedAt = rental.returned_at,
                isReturned = rental.returned_at != null,
            )
    }
}
