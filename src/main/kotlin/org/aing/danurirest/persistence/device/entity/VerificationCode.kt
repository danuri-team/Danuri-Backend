package org.aing.danurirest.persistence.device.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.util.UUID

@RedisHash(value = "verificationCode", timeToLive = 60L * 3)
class VerificationCode(
    @Indexed
    val id: UUID,
    @Id
    val code: String,
)
