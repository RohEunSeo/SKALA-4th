# 과제 6 — Weather Axios (OpenWeather API 연동)

## 📌 과제 개요

기존 mockData(하드코딩 배열)를 OpenWeatherMap Current Weather API 실데이터로 교체하는 과제.
Axios + Pinia Store를 조합해서, 앱이 시작될 때 5개 도시의 날씨를 한 번에 받아와
모든 페이지(대시보드/상세/통계)가 같은 데이터를 공유하도록 구성하는 것이 핵심.

---

## 📁 추가/수정 파일 목록

```
src/
├── stores/
│   └── weatherStore.js      ← 신규: OpenWeather API 호출 + 날씨 데이터 전역 관리 Store
├── views/
│   ├── WeatherHomeView.vue  ← 수정: mockData 제거, weatherStore 사용 + 로딩 표시
│   ├── WeatherDetailView.vue← 수정: mockData 제거, weatherStore 사용
│   └── WeatherStatsView.vue ← 수정: 하드코딩 배열 제거, weatherStore 사용
└── main.js                  ← 수정: 앱 시작 시 fetchAllWeather() 1회 호출

.env                          ← 수정: VITE_OPENWEATHER_API_KEY 추가 (Vite는 VITE_ 접두사만 클라이언트에 노출)
package.json                  ← 수정: axios 의존성 추가
```

---

## 🔑 핵심 개념

### API 요청 URL

```
https://api.openweathermap.org/data/2.5/weather?q={영문도시명}&appid={키}&units=metric&lang=kr
```

- `units=metric` → 섭씨(°C) 기준으로 응답
- `lang=kr` → 날씨 설명(`weather[0].description`)을 한국어로 응답

### 도시 매핑

| id | 한글명 | 영문 쿼리 |
|---|---|---|
| city_01 | 서울 | Seoul |
| city_02 | 수원 | Suwon |
| city_03 | 부산 | Busan |
| city_04 | 대전 | Daejeon |
| city_05 | 제주 | Jeju |

### weatherStore 구성

| 구성요소 | 내용 |
|---|---|
| **state** | `weatherList`(도시별 날씨 배열), `isLoading`, `error` |
| **actions** | `fetchAllWeather()` — `Promise.all`로 5개 도시 API를 동시에 호출 후 mockData와 동일한 구조로 매핑 |

```js
weatherList.value = responses.map((res, idx) => ({
  id: cities[idx].id,
  name: cities[idx].name,
  temp: Math.round(res.data.main.temp),        // 소수점 반올림
  status: res.data.weather[0].description,
  humidity: res.data.main.humidity,             // 상세 페이지용 부가 정보
  wind: res.data.wind.speed,
  feel: Math.round(res.data.main.feels_like),
}))
```

- 목록/통계 화면은 `{ id, name, temp, status }`만 사용하므로 기존 mockData 구조와 완전히 동일하게 맞춤
- 상세 화면(`WeatherDetailView`)은 원래 mockData에 `humidity`/`wind`/`feel`도 있었기 때문에, 기존 기능이 깨지지 않도록 API 응답의 `main.humidity`, `wind.speed`, `main.feels_like`도 함께 저장

### 앱 시작 시 1회 호출 + 로딩 처리

`main.js`에서 앱 마운트 직전에 `fetchAllWeather()`를 호출해서 API 요청을 미리 시작함.
다만 응답은 비동기라 페이지 진입 시점에 아직 안 끝났을 수 있어서,
`WeatherHomeView`는 `isLoading`으로 로딩 문구를 보여주고,
`WeatherDetailView`는 `cityData`를 `ref` 대신 `computed`로 바꿔서
`weatherList`가 나중에 채워져도 자동으로 다시 계산되도록 함(단순 `onMounted` 1회 조회로는 API 응답 전 빈 배열을 조회해 "도시를 찾을 수 없음"으로 잘못 표시되는 문제가 있었음).

---

## ✅ 과제 요구사항 구현 결과

1. **Axios 설치** — `npm install axios`
2. **weatherStore.js 작성** — 5개 도시 API를 `Promise.all`로 동시 호출, `{ id, name, temp, status }` 구조로 정규화
3. **WeatherHomeView 수정** — mockData 제거, `weatherList`/`isLoading`을 스토어에서 가져와 사용, `onMounted`에서 `fetchAllWeather()` 호출, 로딩 중 문구 추가
4. **WeatherDetailView 수정** — mockData 제거, `cityId`로 `weatherList`에서 도시 조회
5. **WeatherStatsView 수정** — 하드코딩 배열 제거, `weatherList`를 스토어에서 가져와 통계 계산
6. **main.js 수정** — 앱 마운트 전 `fetchAllWeather()` 1회 호출

기존 검색 필터, 배경 전환, 단위(°C/°F) 변환, 통계 계산 로직은 전혀 손대지 않았고,
데이터 소스만 mockData → 실제 API 응답으로 교체함.

---

## 💡 배운 점

- Pinia Store의 action 안에서 `axios` 여러 호출을 `Promise.all`로 묶으면,
  화면 쪽 코드는 "요청이 몇 개인지" 신경 쓰지 않고 `fetchAllWeather()` 한 번만 호출하면 됨
- Vite는 `.env`의 변수 중 `VITE_` 접두사가 붙은 것만 `import.meta.env`로 클라이언트에 노출한다는 것을 직접 확인함
- mockData(동기 데이터)를 API(비동기 데이터)로 바꿀 때는 단순히 배열 출처만 바꾸는 게 아니라,
  "아직 로딩 중일 수 있다"는 상태를 화면에서 같이 처리해줘야 한다는 것을 체감함
  (`ref` + `onMounted` 1회 조회 → `computed`로 변경한 이유)
