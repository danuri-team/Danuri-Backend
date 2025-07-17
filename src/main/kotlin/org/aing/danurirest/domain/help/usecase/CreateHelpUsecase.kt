package org.aing.danurirest.domain.help.usecase

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.aing.danurirest.persistence.help.entity.HelpHistory
import org.aing.danurirest.persistence.help.repository.HelpHistoryJpaRepository
import org.aing.danurirest.persistence.help.repository.HelpSettingJpaRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateHelpUsecase(
    private val helpSettingJpaRepository: HelpSettingJpaRepository,
    private val helpHistoryJpaRepository: HelpHistoryJpaRepository,
    private val userJpaRepository: UserJpaRepository,
) {
    fun execute() {
        val userId = PrincipalUtil.getUserId()

        val user =
            userJpaRepository.findById(userId).orElseThrow {
                throw CustomException(CustomErrorCode.NOT_FOUND_USER)
            }

        val helpAlertSetting =
            helpSettingJpaRepository.findByCompanyId(user.company.id!!).orElseThrow {
                CustomException(CustomErrorCode.VALIDATION_ERROR)
            }

        if (!helpAlertSetting.enable) {
            throw CustomException(CustomErrorCode.HELP_NOT_ENABLED)
        }

        helpHistoryJpaRepository.save(
            HelpHistory(
                company = user.company,
            ),
        )
    }
}
