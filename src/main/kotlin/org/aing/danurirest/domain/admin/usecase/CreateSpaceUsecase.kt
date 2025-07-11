package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.SpaceRequest
import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.company.repository.CompanyJpaRepository
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateSpaceUsecase(
    private val spaceJpaRepository: SpaceJpaRepository,
    private val companyRepository: CompanyJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: SpaceRequest): SpaceResponse {
        val companyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyRepository
                .findById(companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val space =
            Space(
                company = company,
                name = request.name,
                startAt = request.startAt,
                endAt = request.endAt,
            )

        return SpaceResponse.from(spaceJpaRepository.save(space))
    }
}
