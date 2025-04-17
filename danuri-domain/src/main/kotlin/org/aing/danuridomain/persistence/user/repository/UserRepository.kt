package org.aing.danuridomain.persistence.user.repository

import org.aing.danuridomain.persistence.user.entity.User
import java.util.Optional
import java.util.UUID

interface UserRepository {
    fun findById(userId: UUID): Optional<User>
}
