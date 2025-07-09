package org.aing.danurirest.global.exception

import org.aing.danurirest.global.exception.enums.CustomErrorCode

class CustomException(
    val customErrorCode: CustomErrorCode,
    val detailMessage: String,
) : RuntimeException(detailMessage) {
    constructor(customErrorCode: CustomErrorCode) : this (
        customErrorCode = customErrorCode,
        detailMessage = customErrorCode.message,
    )
}
