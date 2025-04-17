package org.aing.danuridomain.persistence.user.repository.impl

import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.repository.UserJpaRepository
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findById(userId: UUID): Optional<User> = userJpaRepository.findById(userId)
}
