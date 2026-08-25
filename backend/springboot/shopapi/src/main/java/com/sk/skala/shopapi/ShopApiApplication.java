package com.sk.skala.shopapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
		info = @Info(
				title = "SKALA Goods Shop API",
				description = """
						SKALA 부트캠프 수강생들이 이용하는 공식 굿즈/교육용품 온라인 쇼핑몰 API입니다.

						### 테스트 방법
						1. **회원가입** (`POST /api/customers/signup`)으로 계정을 만듭니다.
						2. **로그인** (`POST /api/customers/login`)으로 JWT 토큰을 발급받습니다. 응답의 `data.token` 값을 복사하세요.
						3. 우측 상단 **Authorize** 버튼을 눌러 복사한 토큰 값을 그대로 붙여넣습니다 (`Bearer ` 접두사는 자동으로 붙습니다).
						4. Authorize 후에는 🔒 표시가 붙은 API(주문 생성/취소, 내 정보/주문 조회)도 별도 헤더 입력 없이 바로 **Try it out** 으로 테스트할 수 있습니다.
						5. 상품 조회/등록/수정/삭제와 회원가입/로그인은 인증 없이 바로 테스트 가능합니다.
						""",
				version = "v1"
		)
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT",
		description = "로그인(`/api/customers/login`) 응답으로 받은 JWT를 입력하세요."
)
@EnableJpaAuditing
@SpringBootApplication
public class ShopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApiApplication.class, args);
	}

}
