package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.AdminUpdateRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateAdminUsecase(
    private val adminJpaRepository: AdminJpaRepository,
) {
    fun execute(request: AdminUpdateRequest) {
        val admin =
            adminJpaRepository
                .findById(
                    PrincipalUtil.getUserId(),
                ).orElseThrow { throw CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }

        if (admin.email != request.email && adminJpaRepository.existsByEmail(request.email)) {
            throw CustomException(CustomErrorCode.DUPLICATE_EMAIL)
        }

        admin.email = request.email
        admin.phone = request.phone

        adminJpaRepository.save(admin)
    }
}
