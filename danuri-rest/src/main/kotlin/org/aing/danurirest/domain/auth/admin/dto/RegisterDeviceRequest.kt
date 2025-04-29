package org.aing.danurirest.domain.auth.admin.dto

import java.util.UUID

data class RegisterDeviceRequest(
    val deviceId: UUID,
    val spaceId: UUID
) 