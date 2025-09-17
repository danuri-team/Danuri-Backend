package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.DeleteHelpRequest
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.help.repository.HelpHistoryJpaRepository
import org.springframework.stereotype.Service

@Service
class DeleteHelpUsecase(
    private val helpHistoryJpaRepository: HelpHistoryJpaRepository,
    private val adminJpaRepository: AdminJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    fun execute(request: DeleteHelpRequest) {
        val adminCompanyId = getAdminCompanyIdUsecase.execute()
        val help =
            helpHistoryJpaRepository.findByIdAndCompanyId(request.helpId, adminCompanyId)
                ?: throw CustomException(CustomErrorCode.NOT_FOUND_HELP)

        val adminId = PrincipalUtil.getUserId()
        val admin = adminJpaRepository.findById(adminId).orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }
        help.isResolved = true
        help.checkedAdmin = admin
    }
}
