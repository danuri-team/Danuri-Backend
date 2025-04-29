package org.aing.danurirest.domain.auth.admin.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class UpdateDeviceRequest(
    @field:NotNull(message = "회사 ID는 필수 입력값입니다.")
    val companyId: UUID,
    
    @field:NotNull(message = "공간 ID는 필수 입력값입니다.")
    val spaceId: UUID,
    
    val isActive: Boolean
) 