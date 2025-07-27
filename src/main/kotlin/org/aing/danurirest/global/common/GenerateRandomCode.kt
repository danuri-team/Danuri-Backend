package org.aing.danurirest.global.common

import java.util.*

object GenerateRandomCode {
    fun execute(): String {
        val random = Random()
        return String.format("%06d", random.nextInt(1000000))
    }
}
