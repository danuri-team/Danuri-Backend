package org.aing.danurirest.domain.form.usecase

import org.aing.danurirest.domain.form.dto.CreateSignUpFormResultRequest
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.util.PrincipalUtil
import org.aing.danurirest.global.util.ValidateJsonSchema
import org.aing.danurirest.persistence.form.entity.FormResult
import org.aing.danurirest.persistence.form.repository.FormResultJpaRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateSignUpFormResultUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val formResultJpaRepository: FormResultJpaRepository,
) {
    @Transactional
    fun execute(request: CreateSignUpFormResultRequest) {
        ValidateJsonSchema.execute(request.result)

        val userId = PrincipalUtil.getUserId()
        val user =
            userJpaRepository.findById(userId).orElseThrow { CustomException(CustomErrorCode.NOT_FOUND_USER) }

        if (user.signUpForm != null) {
            throw CustomException(CustomErrorCode.FORM_ALREADY_CREATED)
        }

        val formResult =
            FormResult(
                result = request.result,
                user = user,
                isSignUpResult = true,
            )

        formResultJpaRepository.save(formResult)
    }
}
