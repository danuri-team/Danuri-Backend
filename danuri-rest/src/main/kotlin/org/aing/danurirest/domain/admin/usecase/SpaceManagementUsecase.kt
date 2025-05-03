package org.aing.danurirest.domain.admin.usecase

import org.aing.danuridomain.persistence.company.repository.CompanyRepository
import org.aing.danuridomain.persistence.space.entity.Space
import org.aing.danuridomain.persistence.space.repository.SpaceRepository
import org.aing.danurirest.domain.admin.dto.SpaceRequest
import org.aing.danurirest.domain.admin.dto.SpaceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SpaceManagementUsecase(
    private val spaceRepository: SpaceRepository,
    private val companyRepository: CompanyRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun createSpace(request: SpaceRequest): SpaceResponse {
        val companyId = getAdminCompanyIdUsecase.execute()

        val company =
            companyRepository
                .findById(companyId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }

        val space =
            Space(
                company = company,
                name = request.name,
                start_at = request.startAt,
                end_at = request.endAt,
            )

        return SpaceResponse.from(spaceRepository.save(space))
    }

    fun updateSpace(
        spaceId: UUID,
        request: SpaceRequest,
    ): SpaceResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val updatedSpace =
            Space(
                id = space.id,
                company = space.company,
                name = request.name,
                start_at = request.startAt,
                end_at = request.endAt,
                usage = space.usage,
            )

        return SpaceResponse.from(spaceRepository.update(updatedSpace))
    }

    fun deleteSpace(spaceId: UUID) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        spaceRepository.delete(spaceId)
    }

    fun getSpace(spaceId: UUID): SpaceResponse {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        return SpaceResponse.from(space)
    }

    // 현재 관리자의 회사에 속한 공간 목록 조회
    fun getCurrentAdminCompanySpaces(): List<SpaceResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val spaces = spaceRepository.findByCompanyId(companyId)
        return spaces.map { SpaceResponse.from(it) }
    }
} 
