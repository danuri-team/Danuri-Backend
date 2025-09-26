package org.aing.danurirest.domain.auth.user.usecase

import org.aing.danurirest.domain.auth.user.dto.UserRegisterRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.user.entity.User
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service

@Service
class RegisterUserUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val deviceJpaRepository: DeviceJpaRepository,
) {
    fun execute(request: UserRegisterRequest) {
        if (userJpaRepository.existsByPhone(request.phone)) {
            throw CustomException(CustomErrorCode.DUPLICATE_USER)
        }

        val device =
            deviceJpaRepository.findById(PrincipalUtil.getUserId()).orElseThrow {
                CustomException(CustomErrorCode.NOT_FOUND_DEVICE)
            }

        val user =
            User(
                company = device.company,
                phone = request.phone,
            )

        userJpaRepository.save(user)
    }
}
