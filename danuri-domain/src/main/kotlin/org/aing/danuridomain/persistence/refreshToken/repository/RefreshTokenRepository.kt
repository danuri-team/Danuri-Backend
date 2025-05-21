package org.aing.danuridomain.persistence.refreshToken.repository

import org.aing.danuridomain.persistence.refreshToken.entity.RefreshToken
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RefreshTokenRepository : CrudRepository<RefreshToken, UUID> {
    fun findByMemberId(memberId: Long): RefreshToken?

    fun findByToken(token: String): RefreshToken?
}
