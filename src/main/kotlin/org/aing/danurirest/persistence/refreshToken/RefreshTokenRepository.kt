package org.aing.danurirest.persistence.refreshToken

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRepository(
    private val stringRedisTemplate: StringRedisTemplate,
    @Value("\${jwt.refresh-token-expires}")
    private val refreshTokenExpireAt: Long,
) {
    fun save(
        userId: String,
        refreshToken: String,
        ttl: Long? = null,
    ) {
        val finalTtl = ttl ?: refreshTokenExpireAt
        stringRedisTemplate
            .opsForValue()
            .set("refresh:$refreshToken", userId, finalTtl, TimeUnit.MILLISECONDS)
    }

    fun consume(refreshToken: String): String? = stringRedisTemplate.opsForValue().getAndDelete("refresh:$refreshToken")
}
