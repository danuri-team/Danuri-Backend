package org.aing.danuridomain.persistence.device.repository

import org.aing.danuridomain.persistence.device.entity.Device
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DeviceJpaRepository : JpaRepository<Device, UUID>
