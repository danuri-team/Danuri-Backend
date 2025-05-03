package org.aing.danurirest.domain.auth.device.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danurirest.domain.auth.device.dto.DeviceSpaceInfoResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class FetchCurrentDeviceSpaceUsecase(
    private val deviceRepository: DeviceRepository,
) {
    fun execute(): DeviceSpaceInfoResponse {
        val context = SecurityContextHolder.getContext().authentication.principal as ContextDto

        val device =
            deviceRepository
                .findByDeviceId(context.id!!)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }

        val currentTime = LocalTime.now()
        val isAvailable = currentTime.isAfter(device.space.start_at) && currentTime.isBefore(device.space.end_at)

        return DeviceSpaceInfoResponse(
            deviceId = device.id!!,
            spaceId = device.space.id!!,
            spaceName = device.space.name,
            companyId = device.space.company.id!!,
            companyName = device.space.company.name,
            startAt = device.space.start_at,
            endAt = device.space.end_at,
            isCurrentlyAvailable = isAvailable,
        )
    }
} 
