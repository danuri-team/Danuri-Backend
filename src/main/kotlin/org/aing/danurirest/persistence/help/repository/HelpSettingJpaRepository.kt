package org.aing.danurirest.persistence.help.repository

import org.aing.danurirest.persistence.help.entity.HelpSetting
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HelpSettingJpaRepository : JpaRepository<HelpSetting, UUID>
