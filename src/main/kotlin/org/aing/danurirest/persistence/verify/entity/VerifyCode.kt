package org.aing.danurirest.persistence.verify.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "verifyCode", timeToLive = 60L * 60 * 24 * 7)
class VerifyCode(
    @Indexed
    val code: String,
    @Id
    @Indexed
    val phoneNumber: String,
)
