package org.aing.danurirest.domain.auth.admin.dto

import java.util.UUID

data class DeviceSignInRequest(
    val deviceId: UUID
) 