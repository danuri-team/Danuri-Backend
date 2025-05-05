package org.aing.danurirest.domain.admin.dto

import java.util.UUID

data class RegisterDeviceRequest(
    val deviceId: UUID,
    val spaceId: UUID
) 