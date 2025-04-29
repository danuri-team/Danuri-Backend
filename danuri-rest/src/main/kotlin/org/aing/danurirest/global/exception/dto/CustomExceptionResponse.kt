package org.aing.danurirest.global.exception.dto

import org.aing.danurirest.global.exception.enums.CustomErrorCode

data class CustomExceptionResponse(
    val status: CustomErrorCode,
    val statusMessage: String,
) {
    constructor(customErrorCode: CustomErrorCode) : this(
        status = customErrorCode,
        statusMessage = customErrorCode.message,
    )
}
