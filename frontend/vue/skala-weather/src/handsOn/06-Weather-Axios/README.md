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

## 💡 배운 점 (기본 연동)

- Pinia Store의 action 안에서 `axios` 여러 호출을 `Promise.all`로 묶으면,
  화면 쪽 코드는 "요청이 몇 개인지" 신경 쓰지 않고 `fetchAllWeather()` 한 번만 호출하면 됨
- Vite는 `.env`의 변수 중 `VITE_` 접두사가 붙은 것만 `import.meta.env`로 클라이언트에 노출한다는 것을 직접 확인함
- mockData(동기 데이터)를 API(비동기 데이터)로 바꿀 때는 단순히 배열 출처만 바꾸는 게 아니라,
  "아직 로딩 중일 수 있다"는 상태를 화면에서 같이 처리해줘야 한다는 것을 체감함
  (`ref` + `onMounted` 1회 조회 → `computed`로 변경한 이유)

---

## 🌍 추가 확장 — 세계 도시 검색 & 5일 예보 (OpenWeatherMap API 추가 활용)

기본 연동(위 내용) 이후, OpenWeatherMap이 제공하는 API를 더 활용해서 기능을 확장했다.

### 1. 좌표 기반 조회 — 판교 추가

OpenWeather는 "판교"를 도시명으로 인식하지 못한다(등록된 지명이 아님). 좌표(위도/경도)로는 조회가 되기 때문에, 도시 목록에 `query`(영문 도시명) 대신 `coords`(위도/경도)를 쓰는 항목을 허용하도록 확장했다.

```js
const cities = [
  { id: 'city_00', name: '판교', coords: { lat: 37.3969908, lon: 127.1129974 } },
  { id: 'city_01', name: '서울', query: 'Seoul' },
  // ...
]

axios.get(BASE_URL, {
  params: {
    ...(city.coords ? { lat: city.coords.lat, lon: city.coords.lon } : { q: city.query }),
    appid: API_KEY, units: 'metric', lang: 'kr',
  },
})
```

### 2. 세계 도시 검색 — Current Weather API 추가 호출

기존 6개 등록 도시와 별개로, 대시보드 검색 영역에 한국/세계 토글을 추가했다. "세계"를 선택하면 등록된 도시 목록 대신 영문 도시명을 직접 입력해서 실시간으로 조회하는 검색창으로 바뀐다.

```js
// src/stores/weatherStore.js
const searchWorldCity = async (query) => {
  const trimmed = query.trim()
  if (!trimmed) return
  isWorldSearchLoading.value = true
  worldSearchError.value = null
  try {
    const res = await axios.get(BASE_URL, {
      params: { q: trimmed, appid: API_KEY, units: 'metric', lang: 'kr' },
    })
    worldCityResult.value = normalizeWeather(res, { id: 'world_search', name: res.data.name })
  } catch (e) {
    worldSearchError.value = e
    worldCityResult.value = null
  } finally {
    isWorldSearchLoading.value = false
  }
}
```

기존 6개 도시 조회(`fetchAllWeather`)와 세계 도시 검색이 API 응답을 카드용 객체로 바꾸는 로직이 동일해서, `normalizeWeather(res, { id, name })`라는 공통 함수로 뽑아내 둘 다 재사용하게 정리했다.

> OpenWeather의 Current Weather API는 **도시명**으로만 검색되고 국가명으로는 검색이 안 된다(예: "China"를 입력하면 실제로 멕시코에 있는 "China"라는 지명이 걸림). 그래서 검색창에 "국가명이 아닌 실제 도시명을 입력해 주세요" 안내 문구를 추가해서 혼동을 줄였다.

### 3. 5일 예보 — Forecast API 추가

상세 정보 패널에서 "상세보기"를 누르면 기존 습도/바람 속도 등에 이어 **5일 예보**가 표시된다. OpenWeather의 5일/3시간 간격 예보 API(`/data/2.5/forecast`)는 하루에 8개(3시간 간격) 데이터를 주기 때문에, 날짜별로 정오(12:00)에 가장 가까운 데이터 하나만 뽑아 5일치로 요약했다.

```js
const FORECAST_URL = 'https://api.openweathermap.org/data/2.5/forecast'

const summarizeForecast = (list) => {
  const byDate = new Map()
  for (const entry of list) {
    const date = entry.dt_txt.slice(0, 10)
    const hour = Number(entry.dt_txt.slice(11, 13))
    const existing = byDate.get(date)
    if (!existing || Math.abs(hour - 12) < Math.abs(Number(existing.dt_txt.slice(11, 13)) - 12)) {
      byDate.set(date, entry) // 그 날짜 중 정오에 가장 가까운 데이터로 교체
    }
  }
  return Array.from(byDate.values()).slice(0, 5).map((entry) => ({
    date: entry.dt_txt.slice(0, 10),
    temp: Math.round(entry.main.temp),
    condition: classifyWeather(entry.weather[0].id, entry.wind.speed),
    icon: entry.weather[0].icon,
  }))
}
```

상세보기(`goToDetail`)를 열 때마다 그 도시 기준으로 `fetchForecast(city.id)`를 호출해서 예보를 새로 조회하기 때문에 항상 최신 데이터를 보여준다.

### 곁다리로 고친 것 — Vercel 배포 시 404 라우팅

배포 후 실제로 없는 경로(`/xyz` 등)에 직접 들어가면 Vue Router의 커스텀 404 페이지가 아니라 Vercel 자체 404(`NOT_FOUND`)가 떴다. Vercel이 파일 시스템 기준으로만 라우팅해서, `index.html`을 거치지 않으면 Vue Router가 아예 개입할 기회가 없기 때문. `vercel.json`에 모든 경로를 `index.html`로 돌려보내는 rewrite를 추가해서 해결했다.

```json
{ "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }] }
```

### 추가/수정 파일 목록 (확장분)

```
vercel.json                    ← 신규: Vercel SPA 라우팅 rewrite
src/
├── stores/weatherStore.js     ← 수정: 좌표 기반 조회, searchWorldCity, fetchForecast, normalizeWeather 공통화
└── views/WeatherHomeView.vue  ← 수정: 한국/세계 검색 토글, 세계 도시 검색 UI, 5일 예보 표시
```

### 💡 배운 점 (확장분)

- **같은 API 응답을 여러 곳에서 가공한다면 정규화 함수를 하나로 뽑아두는 게 안전하다.** 등록 도시 조회와 세계 도시 검색이 API 응답을 카드 객체로 바꾸는 로직이 동일해서, `normalizeWeather()`로 합쳐두니 국가 코드(`country`) 필드 하나를 추가할 때도 한 곳만 고치면 됐다.
- **3시간 간격 예보를 그대로 보여주면 정보가 너무 많다.** 5일치를 보여주려면 40개(5일×8회) 데이터를 그대로 나열하는 게 아니라, "하루 대표값 하나"로 요약하는 규칙(정오에 가장 가까운 값)이 필요했다.
- **REST API는 "이름"이 아니라 "정확한 식별자"로 조회해야 한다.** 도시명 검색은 사람이 기대하는 것과 실제 매칭 결과가 다를 수 있다는 걸 "China" 검색 사례로 체감했다. 좌표 기반 조회(판교)처럼, 이름이 애매한 경우 좌표 같은 명확한 식별자를 쓰는 게 안전하다.
- **SPA 배포는 빌드 성공이 끝이 아니다.** 로컬 개발 서버는 히스토리 모드 라우팅을 알아서 처리해주지만, 실제 배포 환경(Vercel 등)은 별도의 rewrite/fallback 설정이 필요하다는 걸 실제로 404를 겪고 나서야 체감했다.
