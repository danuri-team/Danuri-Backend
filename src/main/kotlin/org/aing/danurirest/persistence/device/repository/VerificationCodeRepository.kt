package org.aing.danurirest.persistence.device.repository

import org.aing.danurirest.persistence.device.entity.VerificationCode
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationCodeRepository : CrudRepository<VerificationCode, String> {
    fun findByCode(code: String): VerificationCode?
}
