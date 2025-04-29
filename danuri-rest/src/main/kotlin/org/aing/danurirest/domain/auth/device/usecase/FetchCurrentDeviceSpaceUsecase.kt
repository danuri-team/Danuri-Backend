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
        // SecurityContextHolder에서 현재 인증된 디바이스의 정보를 가져옴
        val context = SecurityContextHolder.getContext().authentication.principal as ContextDto

        // 디바이스 정보 조회
        val deviceId = context.id ?: throw CustomException(CustomErrorCode.UNAUTHORIZED)
        val device =
            deviceRepository
                .findByDeviceId(deviceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }

        // 기기에 할당된 공간 정보 조회
        val space = device.space ?: throw CustomException(CustomErrorCode.DEVICE_NOT_ASSIGNED_TO_SPACE)

        // 현재 시간이 공간 사용 가능 시간인지 확인
        val currentTime = LocalTime.now()
        val isAvailable = currentTime.isAfter(space.start_at) && currentTime.isBefore(space.end_at)

        return DeviceSpaceInfoResponse(
            deviceId = device.id!!,
            spaceId = space.id!!,
            spaceName = space.name,
            companyId = space.company.id!!,
            companyName = space.company.name,
            startAt = space.start_at,
            endAt = space.end_at,
            isCurrentlyAvailable = isAvailable,
        )
    }
} 
