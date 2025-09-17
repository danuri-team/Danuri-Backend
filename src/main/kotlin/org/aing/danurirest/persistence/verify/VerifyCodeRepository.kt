package org.aing.danurirest.persistence.verify

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class VerifyCodeRepository(
    private val stringRedisTemplate: StringRedisTemplate,
    @Value("\${verify-code.expires}")
    private val verifyCodeExpireAt: Long,
) {
    private fun key(code: String) = "verify:$code"

    fun save(
        phoneNumber: String,
        code: String,
        ttl: Long? = null,
    ) {
        val finalTtl = ttl ?: verifyCodeExpireAt
        stringRedisTemplate
            .opsForValue()
            .set(key(code), phoneNumber, finalTtl, TimeUnit.MILLISECONDS)
    }

    fun consume(code: String): String? = stringRedisTemplate.opsForValue().getAndDelete(key(code))
}
