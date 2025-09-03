package org.aing.danurirest.persistence.refreshToken.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.util.UUID

@RedisHash(value = "refreshToken", timeToLive = 60L * 60 * 24 * 7)
class RefreshToken(
    @Indexed
    val memberId: UUID,
    @Id
    @Indexed
    val token: String,
)
