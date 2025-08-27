package org.aing.danurirest.persistence.form.repository

import org.aing.danurirest.persistence.form.entity.FormResult
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FormResultJpaRepository : JpaRepository<FormResult, UUID>
