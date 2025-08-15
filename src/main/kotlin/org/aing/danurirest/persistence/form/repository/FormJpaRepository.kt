package org.aing.danurirest.persistence.form.repository

import org.aing.danurirest.persistence.form.entity.Form
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FormJpaRepository : JpaRepository<Form, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<Form>
}
