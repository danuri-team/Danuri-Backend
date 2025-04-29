package org.aing.danurirest.domain.auth.admin.dto

import org.aing.danuridomain.persistence.device.entity.Device
import java.time.LocalDateTime
import java.util.UUID

data class DeviceResponse(
    val id: UUID,
    val companyId: UUID,
    val companyName: String,
    val spaceId: UUID,
    val spaceName: String,
    val createAt: LocalDateTime,
    val endAt: LocalDateTime? = null,
    val isActive: Boolean
) {
    companion object {
        fun from(device: Device): DeviceResponse {
            return DeviceResponse(
                id = device.id!!,
                companyId = device.company.id!!,
                companyName = device.company.name,
                spaceId = device.space.id!!,
                spaceName = device.space.name,
                createAt = device.create_at,
                endAt = device.end_at,
                isActive = device.end_at == null
            )
        }
    }
} 