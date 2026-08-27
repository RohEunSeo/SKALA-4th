# 과제 4 — Weather Router

## 📌 과제 개요

과제 3(Component)까지는 화면이 하나(`WeatherParent.vue`)뿐이었는데, 이 과제부터 Vue Router로 여러 페이지를 오가는 실제 앱 구조로 바뀐다. 그래서 이 폴더에는 별도 데모 파일이 없고, 이때 잡힌 구조가 프로젝트의 **실제 앱 구조**(`src/main.js`, `src/App.vue`, `src/router/`, `src/views/`)로 그대로 이어진다.

---

## ✅ 폴더 구조

```
src/
├── main.js                  # 라우터 인스턴스 전역 주입 (app.use(router))
├── App.vue                  # 내비게이션 바(RouterLink) + 메인 콘텐츠 영역(RouterView) 배치
├── router/
│   └── index.js             # 라우트 규칙 정의 + Lazy Loading
└── views/                   # 페이지 단위 컴포넌트
    ├── WeatherHomeView.vue      # '/' — 메인 날씨 대시보드 (WeatherParent 대체)
    ├── WeatherAboutView.vue     # '/about' — 서비스 소개 페이지
    ├── WeatherDetailView.vue    # '/weather/:cityId' — 도시별 상세 페이지 (동적 라우트)
    ├── WeatherStatsView.vue     # '/stats' — 본인 추가 페이지 (날씨 통계)
    └── NotFoundView.vue         # '/:pathMatch(.*)*' — Catch-all 404 페이지
```

---

## ✅ 과제 요구사항 구현

### 1. 라우트 설정 — Lazy Loading + Catch-all

```js
// src/router/index.js
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: () => import('../views/WeatherHomeView.vue') },
    { path: '/about', name: 'about', component: () => import('../views/WeatherAboutView.vue') },
    { path: '/weather/:cityId', name: 'weather-detail', component: () => import('../views/WeatherDetailView.vue') },
    { path: '/stats', name: 'stats', component: () => import('../views/WeatherStatsView.vue') },
    // Catch-all은 반드시 배열 맨 마지막에 위치해야 함
    { path: '/:pathMatch(.*)*', name: 'not-found', component: () => import('../views/NotFoundView.vue') },
  ],
})
```

`component: () => import(...)`처럼 화살표 함수로 감싸면, 그 경로에 실제로 접근할 때만 해당 컴포넌트 코드가 다운로드된다(Lazy Loading).

### 2. App.vue — 내비게이션 바 + RouterView

```html
<nav class="navbar">
  <RouterLink to="/">날씨 대시보드</RouterLink>
  <RouterLink to="/about">서비스 소개</RouterLink>
</nav>
<main>
  <RouterView />
</main>
```

`<RouterLink>`는 `<a>` 태그와 달리 페이지 새로고침 없이 이동하고, 현재 경로와 일치하면 `router-link-active` 클래스가 자동으로 붙는다.

### 3. WeatherHomeView — WeatherParent 대체

과제 3의 `WeatherParent.vue` 로직을 그대로 가져오되, 상세보기 클릭 시 `window.alert()` 대신 실제 페이지 이동으로 바꿨다.

```js
// window.alert(...) 대신
router.push('/weather/' + city.id)
```

### 4. WeatherDetailView — 동적 라우트

`/weather/city_01`처럼 URL의 `:cityId` 부분을 `route.params.cityId`로 읽어서, 그 도시에 해당하는 데이터를 화면에 보여준다.

```js
const cityId = route.params.cityId
cityData.value = mockData.find((c) => c.id === cityId) ?? null
```

### 5. 본인 추가 페이지 — WeatherStatsView (`/stats`)

전체 도시의 평균 기온, 가장 더운/선선한 도시, 더운·선선한 도시 수를 보여주는 통계 페이지를 추가하고 라우팅을 연결했다.

---

## 💡 배운 점

- 하나의 거대한 컴포넌트로 모든 화면을 처리하던 방식에서, "경로 = 화면"으로 역할이 나뉘니 각 view가 뭘 책임지는지 훨씬 명확해졌다.
- Catch-all 라우트(`/:pathMatch(.*)*`)는 반드시 `routes` 배열의 **마지막**에 있어야 한다 — 먼저 정의하면 다른 모든 라우트를 가로채버린다.
