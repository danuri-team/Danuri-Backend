package org.aing.danurirest.persistence.verify.repository

import org.aing.danurirest.persistence.refreshToken.entity.VerifyCode
import org.springframework.data.repository.CrudRepository

interface VerifyCodeRepository : CrudRepository<VerifyCode, String> {
    fun findByPhoneNumber(phone: String): VerifyCode?
}
