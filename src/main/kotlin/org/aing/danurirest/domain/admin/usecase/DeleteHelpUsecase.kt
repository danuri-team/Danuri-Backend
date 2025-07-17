package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.DeleteHelpRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.aing.danurirest.persistence.admin.repository.AdminJpaRepository
import org.aing.danurirest.persistence.help.repository.HelpHistoryJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteHelpUsecase(
    private val helpHistoryJpaRepository: HelpHistoryJpaRepository,
    private val getCurrentAdminUsecase: GetCurrentAdminUsecase,
    private val adminJpaRepository: AdminJpaRepository,
) {
    @Transactional
    fun execute(request: DeleteHelpRequest) {
        val help = helpHistoryJpaRepository.findById(request.helpId).orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_HELP) }
        val adminId = PrincipalUtil.getUserId()
        val admin = adminJpaRepository.findById(adminId).orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_ADMIN) }
        help.isResolved = true
        help.checkedAdmin = admin
    }
}
