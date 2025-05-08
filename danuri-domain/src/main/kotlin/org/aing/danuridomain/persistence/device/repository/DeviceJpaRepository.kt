package org.aing.danuridomain.persistence.device.repository

import org.aing.danuridomain.persistence.device.entity.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface DeviceJpaRepository : JpaRepository<Device, UUID> {
    @Query("SELECT d FROM Device d WHERE d.company.id = :companyId")
    fun findAllByCompanyId(
        @Param("companyId") companyId: UUID,
    ): List<Device>
}
