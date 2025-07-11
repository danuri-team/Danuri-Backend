package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.aing.danurirest.persistence.item.ItemStatus

data class ItemRequest(
    @field:NotBlank(message = "품목 이름은 공백일 수 없습니다.")
    @field:NotNull(message = "품목 이름은 필수 입력값입니다.")
    @field:Size(min = 1, max = 50, message = "품목 이름은 1-50자여야 합니다.")
    val name: String,
    @field:NotNull(message = "총 수량은 필수 입력값입니다.")
    @field:Min(value = 1, message = "총 수량은 최소 1개 이상이어야 합니다.")
    val totalQuantity: Int,
    @field:NotNull(message = "이용 가능 수량은 필수 입력값입니다.")
    @field:Min(value = 1, message = "이용 가능 수량은 최소 1개 이상이어야 합니다.")
    val availableQuantity: Int?,
    @field:NotNull(message = "상태 정보는 필수 입력값입니다.")
    val status: ItemStatus,
)
