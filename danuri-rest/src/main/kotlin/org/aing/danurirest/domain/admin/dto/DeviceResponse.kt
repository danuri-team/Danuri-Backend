package org.aing.danurirest.domain.admin.dto

import org.aing.danuridomain.persistence.device.entity.Device
import java.time.LocalDateTime
import java.util.UUID

data class DeviceResponse(
    val id: UUID,
    val companyId: UUID,
    val companyName: String,
    val createAt: LocalDateTime,
    val endAt: LocalDateTime? = null,
) {
    companion object {
        fun from(device: Device): DeviceResponse =
            DeviceResponse(
                id = device.id!!,
                companyId = device.company.id!!,
                companyName = device.company.name,
                createAt = device.createdAt,
            )
    }
}
