package org.aing.danurirest.persistence.space.repository

import org.aing.danurirest.persistence.space.entity.Space
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpaceJpaRepository : JpaRepository<Space, UUID> {
    fun findAllByCompanyId(
        companyId: UUID,
        pageable: Pageable,
    ): Page<Space>

    fun findByIdAndCompanyId(
        id: UUID,
        companyId: UUID,
    ): Space?
}
