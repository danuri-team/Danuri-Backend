package org.aing.danuridomain.persistence.user.repository

import org.aing.danuridomain.persistence.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<User, UUID>
