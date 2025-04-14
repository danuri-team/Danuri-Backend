package org.aing.danuridomain.persistence.admin.repository

import org.aing.danuridomain.persistence.item.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdminJpaRepository: JpaRepository<Item, UUID>