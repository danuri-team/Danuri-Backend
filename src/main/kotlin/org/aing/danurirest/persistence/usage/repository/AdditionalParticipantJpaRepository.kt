package org.aing.danurirest.persistence.usage.repository

import org.aing.danurirest.persistence.usage.entity.AdditionalParticipant
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdditionalParticipantJpaRepository : JpaRepository<AdditionalParticipant, UUID>