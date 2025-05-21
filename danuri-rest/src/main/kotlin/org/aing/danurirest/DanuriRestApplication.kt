package org.aing.danurirest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication(scanBasePackages = ["org.aing.danuridomain", "org.aing.danurirest"])
class DanuriRestApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    runApplication<DanuriRestApplication>(*args)
}
