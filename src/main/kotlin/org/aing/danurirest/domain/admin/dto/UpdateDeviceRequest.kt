package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateDeviceRequest(
    @field:NotNull(message = "공간 ID는 필수 입력값입니다.")
    @field:NotBlank(message = "별명은 공백일 수 없습니다.")
    val name: String,
) 
