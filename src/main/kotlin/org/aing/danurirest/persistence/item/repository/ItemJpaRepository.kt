package org.aing.danurirest.persistence.item.repository

import jakarta.persistence.LockModeType
import org.aing.danurirest.persistence.item.ItemStatus
import org.aing.danurirest.persistence.item.entity.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ItemJpaRepository : JpaRepository<Item, UUID> {
    fun findAllByCompanyId(
        companyId: UUID,
        pageable: Pageable,
    ): Page<Item>

    fun findByCompanyIdAndAvailableQuantityGreaterThanAndStatus(
        companyId: UUID,
        availableQuantity: Int = 0,
        status: ItemStatus = ItemStatus.AVAILABLE,
    ): List<Item>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id AND i.company.id = :companyId")
    fun findByIdAndCompanyIdWithLock(
        @Param("id") id: UUID,
        @Param("companyId") companyId: UUID,
    ): Item?

    fun findByIdAndCompanyId(
        id: UUID,
        companyId: UUID,
    ): Item?
}
