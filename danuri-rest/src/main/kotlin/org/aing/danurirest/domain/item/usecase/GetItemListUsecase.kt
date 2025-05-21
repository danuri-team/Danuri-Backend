package org.aing.danurirest.domain.item.usecase

import org.aing.danuridomain.persistence.device.repository.DeviceRepository
import org.aing.danuridomain.persistence.item.repository.ItemRepository
import org.aing.danurirest.domain.item.dto.ItemListResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class GetItemListUsecase(
    private val itemRepository: ItemRepository,
    private val deviceRepository: DeviceRepository,
) {
    fun execute(): List<ItemListResponse> {
        val deviceContextDto: ContextDto = SecurityContextHolder.getContext().authentication.principal as ContextDto
        val device =
            deviceRepository.findByDeviceId(deviceId = deviceContextDto.id!!).orElseThrow {
                CustomException(CustomErrorCode.NOT_FOUND_DEVICE)
            }
        val result = itemRepository.findByCompanyIdAndAvailableQuantityGreaterThanAndStatus(device.company.id!!)
        return result.map { item -> ItemListResponse.from(item) }
    }
}
