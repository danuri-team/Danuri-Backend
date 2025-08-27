package org.aing.danurirest.global.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode

object ValidateJsonSchema {
    private val objectMapper = ObjectMapper()

    fun execute(schema: String) {
        try {
            objectMapper.readTree(schema)
        } catch (e: Exception) {
            throw CustomException(CustomErrorCode.INVALID_JSON_SCHEMA)
        }
    }
}
