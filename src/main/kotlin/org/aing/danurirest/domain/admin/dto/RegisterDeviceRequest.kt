package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RegisterDeviceRequest(
    @field:NotNull(message = "디바이스 ID는 필수 입력값입니다.")
    val deviceId: UUID,
    @field:NotNull(message = "기기 별명은 필수 입력값입니다.")
    @field:NotBlank(message = "기기 별명은 공백이 될 수 없습니다.")
    val name: String,
)
