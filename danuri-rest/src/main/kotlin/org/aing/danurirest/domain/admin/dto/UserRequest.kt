package org.aing.danurirest.domain.admin.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.aing.danuridomain.persistence.user.enum.Age
import org.aing.danuridomain.persistence.user.enum.Sex
import java.util.UUID

data class UserRequest(
    @field:NotNull(message = "회사 ID는 필수 입력값입니다.")
    val companyId: UUID,
    
    @field:NotBlank(message = "이름은 필수 입력값입니다.")
    @field:Size(min = 2, max = 20, message = "이름은 2-20자여야 합니다.")
    @field:Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "이름은 한글 또는 영문만 입력 가능합니다.")
    val name: String,
    
    @field:NotNull(message = "성별은 필수 입력값입니다.")
    val sex: Sex,
    
    @field:NotNull(message = "연령대는 필수 입력값입니다.")
    val age: Age,
    
    @field:NotBlank(message = "전화번호는 필수 입력값입니다.")
    @field:Pattern(
        regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$",
        message = "올바른 전화번호 형식이 아닙니다."
    )
    val phone: String
) 