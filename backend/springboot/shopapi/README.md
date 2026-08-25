# SKALA Goods Shop API (skala-shop-api)

SKALA 부트캠프 수강생들이 이용하는 공식 굿즈/교육용품 온라인 쇼핑몰 REST API.

## 실행 방법

```bash
./gradlew bootRun
```

- 서버: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:shop`, 계정: `admin` / 비밀번호 없음)
- Swagger UI: http://localhost:8080/swagger-ui.html
- 앱 시작 시 `data.sql`이 실행되어 상품 8종이 자동으로 등록됩니다.

## API 목록

모든 응답은 `{ "code": 200, "message": "SUCCESS", "data": ... }` 형태의 공통 `Response` 포맷으로 감싸집니다. `Authorization: Bearer {token}` 헤더가 필요한 API는 로그인으로 발급받은 JWT를 사용합니다.

| # | Method | Path | 설명 | 인증 |
|---|---|---|---|---|
| 1 | GET | `/api/products?page=0&size=10` | 상품 목록 (페이징) | 불필요 |
| 2 | POST | `/api/products` | 상품 생성 | 불필요 |
| 3 | GET | `/api/products/{id}` | 상품 단건 조회 | 불필요 |
| 4 | PUT | `/api/products/{id}` | 상품 수정 | 불필요 |
| 5 | DELETE | `/api/products/{id}` | 상품 삭제 | 불필요 |
| 6 | POST | `/api/customers/signup` | 회원가입 (가입 시 100만 포인트 자동 지급) | 불필요 |
| 7 | POST | `/api/customers/login` | 로그인 (JWT 발급) | 불필요 |
| 8 | GET | `/api/customers/me` | 내 정보 조회 | 필요 |
| 9 | POST | `/api/customers/orders` | 주문 생성 (포인트 차감) | 필요 |
| 10 | DELETE | `/api/customers/orders/{id}` | 주문 취소 (포인트 환급) | 필요 |
| 11 | GET | `/api/customers/orders?page=0&size=10` | 내 주문 목록 (페이징) | 필요 |

## Postman 테스트 시나리오

1. **회원가입**
   ```
   POST /api/customers/signup
   { "loginId": "skala01", "password": "1234", "name": "홍길동" }
   ```

2. **로그인 → 토큰 획득**
   ```
   POST /api/customers/login
   { "loginId": "skala01", "password": "1234" }
   ```
   응답의 `data.token` 값을 복사해 이후 요청의 `Authorization: Bearer {token}` 헤더에 사용합니다.

3. **상품 목록 조회**
   ```
   GET /api/products
   ```
   상품 `id`를 확인합니다.

4. **주문 생성**
   ```
   POST /api/customers/orders
   Authorization: Bearer {token}
   { "productId": 1, "quantity": 2 }
   ```
   내 포인트가 `상품가격 × 수량`만큼 차감됩니다.

5. **내 정보 조회 (포인트 확인)**
   ```
   GET /api/customers/me
   Authorization: Bearer {token}
   ```

6. **주문 취소**
   ```
   DELETE /api/customers/orders/{orderId}
   Authorization: Bearer {token}
   ```
   차감했던 포인트가 환급됩니다.

7. **내 주문 목록 조회**
   ```
   GET /api/customers/orders
   Authorization: Bearer {token}
   ```

### 예외 상황 확인

- 포인트보다 비싼 주문을 시도하면 `400 INSUFFICIENT_POINT`
- 존재하지 않는 상품/주문 ID를 조회하면 `404 ...NOT_FOUND`
- `Authorization` 헤더 없이 보호된 API를 호출하면 `401 UNAUTHORIZED`
- 이미 취소된 주문을 다시 취소하면 `400 ALREADY_CANCELED`
- 중복된 `loginId`로 회원가입하면 `409 DUPLICATE_LOGIN_ID`
