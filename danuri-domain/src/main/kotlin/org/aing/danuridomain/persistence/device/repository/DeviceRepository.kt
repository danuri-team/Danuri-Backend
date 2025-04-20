package org.aing.danuridomain.persistence.device.repository

import org.aing.danuridomain.persistence.device.entity.Device
import java.util.Optional
import java.util.UUID

interface DeviceRepository {
    fun findByDeviceId(deviceId: UUID): Optional<Device>
}
