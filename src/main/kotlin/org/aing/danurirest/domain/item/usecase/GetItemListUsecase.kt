package org.aing.danurirest.domain.item.usecase

import org.aing.danurirest.domain.item.dto.ItemListResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.springframework.stereotype.Service

@Service
class GetItemListUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val deviceJpaRepository: DeviceJpaRepository,
) {
    fun execute(): List<ItemListResponse> {
        val deviceContextDto = PrincipalUtil.getContextDto()
        val device =
            deviceJpaRepository.findById(deviceContextDto.id!!).orElseThrow {
                CustomException(CustomErrorCode.NOT_FOUND_DEVICE)
            }
        val result = itemJpaRepository.findByCompanyIdAndAvailableQuantityGreaterThanAndStatus(device.company.id!!)
        return result.map { item -> ItemListResponse.from(item) }
    }
}
