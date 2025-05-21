package org.aing.danuridomain.persistence.redis.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class DomainLayerCacheService(
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    fun getFromCache(key: String): Any? = redisTemplate.opsForValue().get(key)

    fun putToCache(
        key: String,
        value: Any,
    ) {
        redisTemplate.opsForValue().set(key, value)
    }

    fun deleteFromCache(key: String) {
        redisTemplate.delete(key)
    }

    fun initCache(pattern: String) {
        val keys = redisTemplate.keys(pattern)
        if (keys.isNotEmpty()) {
            redisTemplate.delete(pattern)
        }
    }
}
