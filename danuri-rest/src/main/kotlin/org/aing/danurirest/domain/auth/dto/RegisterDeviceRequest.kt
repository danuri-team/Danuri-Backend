package org.aing.danurirest.domain.auth.dto

import java.util.UUID

data class RegisterDeviceRequest(
    val deviceId: UUID,
    val companyId: UUID,
    val spaceId: UUID,
)
