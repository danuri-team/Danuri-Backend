package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ItemCreateRequest(
    @field:NotBlank(message = "품목 이름은 공백일 수 없습니다.")
    @field:NotNull(message = "품목 이름은 필수 입력값입니다.")
    @field:Size(min = 1, max = 50, message = "품목 이름은 1-50자여야 합니다.")
    val name: String,
    @field:NotNull(message = "총 수량은 필수 입력값입니다.")
    @field:Min(value = 1, message = "총 수량은 최소 1개 이상이어야 합니다.")
    val totalQuantity: Int,
)
