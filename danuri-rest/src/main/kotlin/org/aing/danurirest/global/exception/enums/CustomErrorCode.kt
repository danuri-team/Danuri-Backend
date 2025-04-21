package org.aing.danurirest.global.exception.enums

import org.springframework.http.HttpStatus

enum class CustomErrorCode(
    val statusCode: HttpStatus,
    val statusMessage: String,
) {
    // COMMON
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 페이지입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "의도하지 않은 오류가 발생했습니다."),
    MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, "본문 요청이 비어있거나 잘못된 형식입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않는 구성의 바디 요청 값 입니다."),
    PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않는 구성의 파라미터 요청 값 입니다."),

    // 인증 & 인가
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 토큰이 존재하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 부족합니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),

    // 공간
    USAGE_CONFLICT_SPACE(HttpStatus.CONFLICT, "공간을 이용할 수 없습니다. 이용 가능 시간과 예약 현황을 확인해 주세요."),

    // 디바이스
    DEVICE_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록 된 기기입니다."),
}
