package com.sk.skala.shopapi.common;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "모든 API가 공통으로 사용하는 응답 포맷")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int code;

    @Schema(description = "성공 시 SUCCESS, 실패 시 에러 메시지", example = "SUCCESS")
    private String message;

    @Schema(description = "실제 응답 데이터 (실패 시 null)")
    private T data;

    public static <T> Response<T> success(T data) {
        return new Response<>(HttpStatus.OK.value(), "SUCCESS", data);
    }

    public static Response<Void> success() {
        return new Response<>(HttpStatus.OK.value(), "SUCCESS", null);
    }

    public static <T> Response<T> of(int code, String message, T data) {
        return new Response<>(code, message, data);
    }
}
