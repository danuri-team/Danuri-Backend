package org.aing.danurirest.persistence.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["org.aing.danurirest.persistence"])
@EntityScan(basePackages = ["org.aing.danurirest.persistence"])
class JpaConfiguration
