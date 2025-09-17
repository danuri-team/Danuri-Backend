package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.help.entity.HelpHistory
import org.aing.danurirest.persistence.help.repository.HelpHistoryJpaRepository
import org.aing.danurirest.persistence.help.repository.HelpSettingJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetHelpUsecase(
    private val helpHistoryJpaRepository: HelpHistoryJpaRepository,
    private val helpSettingJpaRepository: HelpSettingJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional(readOnly = true)
    fun execute(): List<HelpHistory> {
        val companyId = getAdminCompanyIdUsecase.execute()
        val helpSetting =
            helpSettingJpaRepository
                .findByCompanyId(
                    companyId,
                ).orElseThrow { CustomException(CustomErrorCode.NOT_FOUND) }

        if (!helpSetting.enable) {
            throw CustomException(CustomErrorCode.HELP_NOT_ENABLED)
        }

        return helpHistoryJpaRepository.findAllByCompanyId(companyId) // DTO로 변환 필요
    }
}
