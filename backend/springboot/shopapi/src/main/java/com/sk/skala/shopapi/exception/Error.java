package com.sk.skala.shopapi.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum Error {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "고객을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    Error(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
