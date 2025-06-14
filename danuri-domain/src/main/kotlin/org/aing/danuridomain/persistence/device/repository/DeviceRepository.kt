package org.aing.danuridomain.persistence.device.repository

import org.aing.danuridomain.persistence.device.entity.Device
import java.util.Optional
import java.util.UUID

interface DeviceRepository {
    fun findByDeviceId(deviceId: UUID): Optional<Device>

    fun findById(id: UUID): Optional<Device>

    fun save(device: Device): Device

    fun delete(id: UUID)

    fun findByCompanyId(companyId: UUID): List<Device>
}
