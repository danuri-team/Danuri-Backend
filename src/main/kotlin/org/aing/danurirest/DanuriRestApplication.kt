package org.aing.danurirest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import java.util.TimeZone

@EnableFeignClients
@SpringBootApplication
class DanuriRestApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    runApplication<DanuriRestApplication>(*args)
}
