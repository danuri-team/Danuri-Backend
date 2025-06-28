package org.aing.danurirest.persistence.refreshToken.entity

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "refreshToken", timeToLive = 60L * 60 * 24 * 7)
class RefreshToken(
    @Indexed
    val memberId: Long? = null,
    @Id
    @Indexed
    val token: String,
)
