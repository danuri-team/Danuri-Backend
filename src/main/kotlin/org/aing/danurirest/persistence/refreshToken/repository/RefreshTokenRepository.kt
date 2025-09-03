package org.aing.danurirest.persistence.refreshToken.repository

import org.aing.danurirest.persistence.refreshToken.entity.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    fun findByToken(token: String): RefreshToken?
}
