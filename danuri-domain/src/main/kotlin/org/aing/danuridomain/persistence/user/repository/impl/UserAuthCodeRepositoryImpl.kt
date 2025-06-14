package org.aing.danuridomain.persistence.user.repository.impl

import org.aing.danuridomain.persistence.user.entity.UserAuthCode
import org.aing.danuridomain.persistence.user.repository.UserAuthCodeJpaRepository
import org.aing.danuridomain.persistence.user.repository.UserAuthCodeRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
class UserAuthCodeRepositoryImpl(
    private val userAuthCodeJpaRepository: UserAuthCodeJpaRepository,
) : UserAuthCodeRepository {
    @Transactional
    override fun save(userAuthCode: UserAuthCode): UserAuthCode = userAuthCodeJpaRepository.save(userAuthCode)

    override fun findByPhone(phone: String): Optional<UserAuthCode> = userAuthCodeJpaRepository.findByPhone(phone)

    @Transactional
    override fun deleteByPhone(phone: String) {
        userAuthCodeJpaRepository.deleteByPhone(phone)
    }
} 
