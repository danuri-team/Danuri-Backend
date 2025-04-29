package org.aing.danurirest.domain.auth.device.dto

import java.time.LocalTime
import java.util.UUID

data class DeviceSpaceInfoResponse(
    val deviceId: UUID,
    val spaceId: UUID,
    val spaceName: String,
    val companyId: UUID,
    val companyName: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val isCurrentlyAvailable: Boolean
) 