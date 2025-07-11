package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class UpdateDeviceRequest(
    @field:NotNull(message = "공간 ID는 필수 입력값입니다.")
    val spaceId: UUID,
    @field:NotNull(message = "활성화 여부는 필수 입력값입니다.")
    val isActive: Boolean
) 