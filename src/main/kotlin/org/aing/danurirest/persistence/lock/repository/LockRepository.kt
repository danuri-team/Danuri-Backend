package org.aing.danurirest.persistence.lock.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class LockRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    fun lock(key: LockKeys): Boolean =
        redisTemplate
            .opsForValue()
            .setIfAbsent(key.toString(), "lock", Duration.ofMinutes(1))
            ?: false

    fun unlock(key: LockKeys): Boolean = redisTemplate.delete(key.toString())
}
