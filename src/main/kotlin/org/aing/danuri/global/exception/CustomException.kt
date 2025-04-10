package org.aing.danuri.global.exception

import org.aing.danuri.global.exception.enums.CustomErrorCode

class CustomException(
    val customErrorCode: CustomErrorCode,
    val detailMessage: String,
): RuntimeException(detailMessage) {
    constructor(customErrorCode: CustomErrorCode) : this (
        customErrorCode = customErrorCode,
        detailMessage = customErrorCode.statusMessage,
    )
}