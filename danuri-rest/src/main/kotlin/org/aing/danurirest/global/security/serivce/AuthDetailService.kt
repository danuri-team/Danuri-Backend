package org.aing.danurirest.global.security.serivce

import org.aing.danuridomain.persistence.user.entity.User
import org.aing.danuridomain.persistence.user.repository.UserRepository
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthDetailService(
    private val userRepository: UserRepository,
) {
    fun loadUserByUsername(userId: UUID): User =
        userRepository.findById(userId).orElseThrow {
            throw CustomException(CustomErrorCode.NOT_FOUND_USER)
        }
}
