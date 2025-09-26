package org.aing.danurirest.domain.admin.usecase

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.aing.danurirest.domain.admin.dto.FormSchema
import org.aing.danurirest.domain.admin.dto.GetUsersAndFormHeaderList
import org.aing.danurirest.domain.admin.dto.UserResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.repository.FormJpaRepository
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUsersUsecase(
    private val userJpaRepository: UserJpaRepository,
    private val formJpaRepository: FormJpaRepository,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
    private val objectMapper: ObjectMapper,
) {
    @Transactional(readOnly = true)
    fun execute(): GetUsersAndFormHeaderList {
        val companyId = getAdminCompanyIdUsecase.execute()
        val users = userJpaRepository.findAllByCompanyId(companyId)

        val form =
            formJpaRepository
                .findByCompanyIdAndSignUpFormTrue(
                    companyId,
                ).orElseThrow { CustomException(CustomErrorCode.FORM_IS_NOT_SETUP) }

        val json: List<FormSchema> =
            try {
                objectMapper.readValue<List<FormSchema>>(form.formSchema)
            } catch (e: JsonProcessingException) {
                throw CustomException(CustomErrorCode.FORM_IS_NOT_VALID)
            }

        val headerList = json.map { it.label }

        return GetUsersAndFormHeaderList(users.map { UserResponse.from(it) }, headerList)
    }
}
