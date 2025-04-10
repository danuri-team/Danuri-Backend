package org.aing.danuri.global.exception.dto

import org.aing.danuri.global.exception.enums.CustomErrorCode

data class CustomExceptionResponse(
    val status: CustomErrorCode,
    val statusMessage: String
) {
    constructor(customErrorCode: CustomErrorCode) : this(
        status = customErrorCode,
        statusMessage = customErrorCode.statusMessage
    )
}
