package org.aing.danurirest.domain.form.usecase

import org.aing.danurirest.domain.form.dto.GetSignUpFormResponse
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.springframework.stereotype.Service

@Service
class GetSignUpFormUsecase(
    private val formJpaRepository: FormJpaRepository,
    private val deviceJpaRepository: DeviceJpaRepository,
) {
    fun execute(): GetSignUpFormResponse {
        val deviceId = PrincipalUtil.getUserId()
        val device = deviceJpaRepository.findById(deviceId).orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_DEVICE) }
        val form =
            formJpaRepository.findByCompanyIdAndSignUpFormTrue(device.company.id!!).orElseThrow {
                CustomException(CustomErrorCode.FORM_IS_NOT_SETUP)
            }

        return GetSignUpFormResponse.from(form)
    }
}
