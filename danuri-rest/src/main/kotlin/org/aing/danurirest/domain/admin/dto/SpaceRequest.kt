package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalTime
import java.util.UUID

data class SpaceRequest(
    @field:NotNull(message = "회사 ID는 필수 입력값입니다.")
    val companyId: UUID,
    
    @field:NotBlank(message = "공간 이름은 필수 입력값입니다.")
    @field:Size(min = 1, max = 50, message = "공간 이름은 1-50자여야 합니다.")
    val name: String,
    
    @field:NotNull(message = "시작 시간은 필수 입력값입니다.")
    val startAt: LocalTime,
    
    @field:NotNull(message = "종료 시간은 필수 입력값입니다.")
    val endAt: LocalTime
) 