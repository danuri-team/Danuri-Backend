package org.aing.danurirest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan("org.aing.danuridomain.persistence")
class DanuriRestApplication

fun main(args: Array<String>) {
    runApplication<DanuriRestApplication>(*args)
}
