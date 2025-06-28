package org.aing.danurirest.persistence.user.repository

import org.aing.danurirest.persistence.user.entity.UserAuthCode
import java.util.Optional

interface UserAuthCodeRepository {
    fun save(userAuthCode: UserAuthCode): UserAuthCode

    fun findByPhone(phone: String): Optional<UserAuthCode>

    fun deleteByPhone(phone: String)
}
