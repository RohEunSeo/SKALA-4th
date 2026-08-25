package com.sk.skala.shopapi.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원가입/로그인 공용 요청 (로그인 시 name은 무시됩니다)")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @Schema(description = "로그인 아이디", example = "skala01")
    @NotBlank
    private String loginId;

    @Schema(description = "비밀번호", example = "1234")
    @NotBlank
    private String password;

    @Schema(description = "이름 (회원가입 시 필수, 로그인 요청에서는 무시됨)", example = "홍길동")
    private String name;
}
