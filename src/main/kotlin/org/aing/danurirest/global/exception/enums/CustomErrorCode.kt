package org.aing.danurirest.global.exception.enums

import org.springframework.http.HttpStatus

enum class CustomErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    // Common
    UNKNOWN_SERVER_ERROR(HttpStatus.BAD_GATEWAY, "알 수 없는 문제가 발생했습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND, "엔드포인트를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다."),
    MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 바디가 누락되었습니다."),

    // Auth
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 필요합니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 인증번호입니다."),
    TOO_MANY_REQUESTS(HttpStatus.BAD_REQUEST, "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),
    ALREADY_SENT_VERIFY_CODE(HttpStatus.CONFLICT, "이미 인증번호가 발송되었습니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 등록된 사용자입니다."),

    // Admin
    NEED_COMPANY_APPROVE(HttpStatus.UNAUTHORIZED, "회사 확인이 필요합니다. 관리자에게 문의해주세요."),
    NOT_FOUND_ADMIN(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."),

    // Company
    NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, "회사를 찾을 수 없습니다."),
    COMPANY_MISMATCH(HttpStatus.FORBIDDEN, "해당 회사의 리소스에 접근할 권한이 없습니다."),

    // Item
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "품목을 찾을 수 없습니다."),
    ITEM_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "현재 대여할 수 없는 품목입니다."),
    INSUFFICIENT_ITEM_QUANTITY(HttpStatus.BAD_REQUEST, "남은 수량이 부족합니다."),
    NOT_RENTED_ITEM(HttpStatus.NOT_FOUND, "대여했던 품목을 찾을 수 없습니다."),
    ALREADY_RETURNED_ITEM(HttpStatus.BAD_REQUEST, "모든 대여건이 반납 되었습니다."),

    // Space
    NOT_FOUND_SPACE(HttpStatus.NOT_FOUND, "공간을 찾을 수 없습니다."),
    SPACE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "이용 가능한 시간이 아닙니다."),
    ALREADY_END(HttpStatus.BAD_REQUEST, "이미 종료된 공간 이용 기록입니다."),

    // Usage
    USAGE_EXPIRED(HttpStatus.BAD_REQUEST, "이미 만료된 공간 이용입니다."),
    USAGE_CONFLICT_SPACE(HttpStatus.CONFLICT, "공간 이용 시간에 충돌이 발생했습니다."),
    USAGE_CONFLICT_USER(HttpStatus.CONFLICT, "이미 다른 공간을 사용 중입니다."),
    NOT_USAGE_FOUND(HttpStatus.NOT_FOUND, "공간을 사용 중이지 않습니다."),

    // Device
    NOT_FOUND_DEVICE(HttpStatus.NOT_FOUND, "기기를 찾을 수 없습니다."),
    DEVICE_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 기기입니다."),

    // Help
    NOT_FOUND_HELP(HttpStatus.NOT_FOUND, "도움을 찾을 수 없습니다."),
    HELP_NOT_ENABLED(HttpStatus.BAD_GATEWAY, "도움 기능이 활성화되어 있지 않습니다."),

    // Form
    FORM_IS_NOT_SETUP(HttpStatus.NOT_FOUND, "폼 설정이 되어 있지 않습니다."),
    FORM_IS_NOT_VALID(HttpStatus.INTERNAL_SERVER_ERROR, "폼이 올바르게 저장되지 않았어요. 문의를 통해 해결해주세요."),
    NOT_SIGNED_UP(HttpStatus.BAD_REQUEST, "이용 전 회원가입 폼 입력은 필수입니다."),
    FORM_ALREADY_SETUP(HttpStatus.CONFLICT, "이미 가입 폼 설정이 되어 있습니다. 해제 후 다시 설정 해주세요."),
    FORM_ALREADY_CREATED(HttpStatus.CONFLICT, "이미 폼 입력이 완료되어 있습니다."),
}
