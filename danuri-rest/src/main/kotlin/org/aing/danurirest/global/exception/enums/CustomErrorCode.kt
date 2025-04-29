package org.aing.danurirest.global.exception.enums

import org.springframework.http.HttpStatus

enum class CustomErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    // Common
    UNKNOWN_SERVER_ERROR(HttpStatus.BAD_GATEWAY, "알 수 없는 문제가 발생 했습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다."),
    MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, "잘못된 리퀘스트 바디입니다"),

    // Auth
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 access token 입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 access token 입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh token 입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 refresh token 입니다."),
    TOO_MANY_REQUESTS(HttpStatus.BAD_REQUEST, "잠시 후에 다시 시도 해주세요."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 등록된 사용자입니다."),
    USER_HAS_USAGES(HttpStatus.CONFLICT, "사용자의 이용 내역이 있어 삭제할 수 없습니다."),

    // Admin
    NOT_FOUND_ADMIN(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),

    // Company
    NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, "회사를 찾을 수 없습니다."),
    COMPANY_MISMATCH(HttpStatus.FORBIDDEN, "해당 회사의 리소스에 접근할 권한이 없습니다."),

    // Item
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "품목을 찾을 수 없습니다."),
    ITEM_IN_USE(HttpStatus.CONFLICT, "사용 중인 품목이 있어 삭제할 수 없습니다."),
    ITEM_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "현재 대여할 수 없는 아이템입니다."),
    INSUFFICIENT_ITEM_QUANTITY(HttpStatus.BAD_REQUEST, "남은 수량이 부족합니다."),
    ALREADY_RETURNED(HttpStatus.BAD_REQUEST, "이미 반납된 대여 기록입니다."),
    OVER_QUANTITY(HttpStatus.BAD_REQUEST, "반납 수량이 대여 수량을 초과합니다."),

    // Space
    NOT_FOUND_SPACE(HttpStatus.NOT_FOUND, "해당 공간을 찾을 수 없습니다."),
    SPACE_IN_USE(HttpStatus.BAD_REQUEST, "공간이 현재 사용 중입니다."),
    NO_OWN_SPACE_OR_AVAILABLE(HttpStatus.BAD_REQUEST, "존재하지 않는 대여 기록입니다."),
    ALREADY_END(HttpStatus.BAD_REQUEST, "이미 종료된 공간 사용 기록입니다."),

    // Usage
    USAGE_CONFLICT_SPACE(HttpStatus.CONFLICT, "이용 충돌이 발생 했습니다."),

    // Device
    NOT_FOUND_DEVICE(HttpStatus.NOT_FOUND, "해당 기기를 찾을 수 없습니다."),
    DEVICE_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 기기입니다."),
    DEVICE_NOT_ASSIGNED_TO_SPACE(HttpStatus.BAD_REQUEST, "해당 기기가 공간에 할당되지 않았습니다."),
}
