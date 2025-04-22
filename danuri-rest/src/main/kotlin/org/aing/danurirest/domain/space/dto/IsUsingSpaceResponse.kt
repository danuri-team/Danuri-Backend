package org.aing.danurirest.domain.space.dto

import jakarta.validation.constraints.NotNull

data class IsUsingSpaceResponse(
    @field:NotNull(message = "공간 사용 여부는 필수입니다.")
    val isUsingSpace: Boolean,
)
