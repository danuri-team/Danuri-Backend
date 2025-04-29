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
        // 현재 관리자의 회사 ID 가져오기
        val companyId = getAdminCompanyIdUsecase.execute()

        // 요청된 회사 ID가 관리자의 회사와 일치하는지 확인
        if (request.companyId != companyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val company =
            companyRepository
                .findById(request.companyId)
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
        // 현재 관리자의 회사 ID 가져오기
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        // 현재 공간이 관리자의 회사에 속하는지 확인
        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        // 요청된 회사 ID가 관리자의 회사와 일치하는지 확인
        if (request.companyId != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        // 회사가 변경되었는지 확인
        val company =
            if (space.company.id != request.companyId) {
                companyRepository
                    .findById(request.companyId)
                    .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_COMPANY) }
            } else {
                space.company
            }

        val updatedSpace =
            Space(
                id = space.id,
                company = company,
                name = request.name,
                start_at = request.startAt,
                end_at = request.endAt,
                usage = space.usage,
            )

        return SpaceResponse.from(spaceRepository.update(updatedSpace))
    }

    fun deleteSpace(spaceId: UUID) {
        // 현재 관리자의 회사 ID 가져오기
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        // 공간이 관리자의 회사에 속하는지 확인
        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        // 사용 중인 공간인지 확인
        if (space.usage.isNotEmpty()) {
            throw CustomException(CustomErrorCode.SPACE_IN_USE)
        }

        spaceRepository.delete(spaceId)
    }

    fun getSpace(spaceId: UUID): SpaceResponse {
        // 현재 관리자의 회사 ID 가져오기
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        val space =
            spaceRepository
                .findById(spaceId)
                .orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_SPACE) }

        // 공간이 관리자의 회사에 속하는지 확인
        if (space.company.id != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        return SpaceResponse.from(space)
    }

    fun getSpacesByCompany(companyId: UUID): List<SpaceResponse> {
        // 현재 관리자의 회사 ID 가져오기
        val adminCompanyId = getAdminCompanyIdUsecase.execute()

        // 요청된 회사 ID가 관리자의 회사와 일치하는지 확인
        if (companyId != adminCompanyId) {
            throw CustomException(CustomErrorCode.COMPANY_MISMATCH)
        }

        val spaces = spaceRepository.findByCompanyId(companyId)
        return spaces.map { SpaceResponse.from(it) }
    }

    // 현재 관리자의 회사에 속한 공간 목록 조회
    fun getCurrentAdminCompanySpaces(): List<SpaceResponse> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val spaces = spaceRepository.findByCompanyId(companyId)
        return spaces.map { SpaceResponse.from(it) }
    }
} 
