package org.aing.danuridomain.persistence.device.repository.impl

import org.aing.danuridomain.persistence.device.entity.Device
import org.aing.danuridomain.persistence.device.repository.DeviceJpaRepository
import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class DeviceRepositoryImpl(
    private val deviceJpaRepository: DeviceJpaRepository,
) : DeviceRepository {
    override fun findByDeviceId(deviceId: UUID): Optional<Device> = deviceJpaRepository.findById(deviceId)

    override fun findById(id: UUID): Optional<Device> = deviceJpaRepository.findById(id)

    override fun save(device: Device): Device = deviceJpaRepository.save(device)
    
    override fun update(device: Device): Device = deviceJpaRepository.save(device)

    override fun delete(id: UUID) = deviceJpaRepository.deleteById(id)

    override fun findAll(): List<Device> = deviceJpaRepository.findAll()

    override fun findByCompanyId(companyId: UUID): List<Device> = 
        deviceJpaRepository.findAllByCompanyId(companyId)

    override fun findBySpaceId(spaceId: UUID): List<Device> = 
        deviceJpaRepository.findAllBySpaceId(spaceId)
}
