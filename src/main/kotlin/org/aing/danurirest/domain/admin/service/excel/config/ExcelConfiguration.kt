package org.aing.danurirest.domain.admin.service.excel.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableConfigurationProperties(ExcelProperties::class)
@EnableAspectJAutoProxy
class ExcelConfiguration
