package org.aing.danurirest.domain.usage.dto

import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex

data class AdditionalParticipantsDto(
    val sex: Sex,
    val ageGroup: Age,
    val count: Int,
)
