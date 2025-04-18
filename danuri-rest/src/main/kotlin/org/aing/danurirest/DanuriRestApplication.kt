package org.aing.danurirest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.aing.danuridomain", "org.aing.danurirest"])
class DanuriRestApplication

fun main(args: Array<String>) {
    runApplication<DanuriRestApplication>(*args)
}
