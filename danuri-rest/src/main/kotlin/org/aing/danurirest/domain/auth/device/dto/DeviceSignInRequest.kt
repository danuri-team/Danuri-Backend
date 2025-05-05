package org.aing.danurirest.domain.auth.device.dto

import java.util.UUID

data class DeviceSignInRequest(
    val deviceId: UUID
) 