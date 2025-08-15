package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.ItemCreateRequest
import org.aing.danurirest.domain.admin.dto.ItemResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.item.entity.Item
import org.aing.danurirest.persistence.item.repository.ItemJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateItemUsecase(
    private val itemJpaRepository: ItemJpaRepository,
    private val companyRepository: CompanyJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: ItemCreateRequest): ItemResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyRepository
                .findById(adminCompanyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val item =
            Item(
                company = company,
                name = request.name,
                totalQuantity = request.totalQuantity,
                availableQuantity = request.totalQuantity,
                status = ItemStatus.AVAILABLE,
            )

        return ItemResponse.from(itemJpaRepository.save(item))
    }
}
