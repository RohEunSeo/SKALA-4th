package com.sk.skala.shopapi.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인한 고객 정보. 로그인 응답에는 JWT 토큰이 함께 담깁니다.")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSession {

    @Schema(description = "고객 ID", example = "1")
    private Long customerId;

    @Schema(description = "로그인 아이디", example = "skala01")
    private String loginId;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "JWT 액세스 토큰 (로그인 응답에서만 채워짐). 이 값을 Swagger 상단의 Authorize에 입력하세요.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
}
