package org.aing.danuridomain.persistence.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["org.aing.danuridomain.persistence"])
@EntityScan(basePackages = ["org.aing.danuridomain.persistence"])
class JpaConfiguration
