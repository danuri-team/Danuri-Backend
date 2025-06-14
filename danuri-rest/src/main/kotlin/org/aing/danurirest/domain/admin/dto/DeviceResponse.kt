package org.aing.danurirest.domain.admin.dto

import org.aing.danuridomain.persistence.device.entity.Device
import java.time.LocalDateTime
import java.util.UUID

data class DeviceResponse(
    val id: UUID,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(device: Device): DeviceResponse =
            DeviceResponse(
                id = device.id!!,
                createdAt = device.createdAt!!,
            )
    }
}
