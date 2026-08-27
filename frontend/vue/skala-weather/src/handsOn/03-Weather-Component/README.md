# 과제 3 — Weather Component

## 📌 과제 개요

과제 2(Composition)의 단일 파일 화면을, 기능 변경 없이 4개의 컴포넌트로 분리하는 과제. `props`/`emit`으로 부모-자식 간 데이터 흐름을 명확히 하고, `<slot>`으로 공통 디자인 껍데기를 재사용하는 걸 목표로 한다.

---

## ✅ 컴포넌트 구조

| 파일 | 역할 |
|---|---|
| `WeatherParent.vue` | 모든 반응형 상태(`weatherList`, `searchQuery`, `selectedCityInfo` 등)를 관리하는 최상위 컴포넌트. 자식이 emit한 이벤트를 받아서 상태를 바꾼다 |
| `BaseDashboardCard.vue` | 데이터가 없는 "글래스 카드 껍데기"만 담당. `<slot>` 하나로 검색 영역/목록 영역 양쪽에 재사용됨 |
| `SearchBar.vue` | `searchQuery`를 **props**로 받아 입력창에 표시, 타이핑하면 `update-query` 이벤트로 **emit** |
| `WeatherCard.vue` | 도시 객체 하나를 **props**로 받아 카드로 표시. 카드 클릭은 `select-card`, 상세보기 클릭은 `click-detail`로 **emit** |

### props / emit 흐름

```html
<!-- WeatherParent.vue -->
<BaseDashboardCard>
  <SearchBar :search-query="searchQuery" @update-query="handleUpdateQuery" />
</BaseDashboardCard>

<BaseDashboardCard>
  <WeatherCard
    v-for="city in filteredWeatherList"
    :key="city.id"
    :city="city"
    @select-card="handleSelectCard"
    @click-detail="handleClickDetail"
  />
</BaseDashboardCard>
```

- `SearchBar`, `WeatherCard`는 데이터를 직접 들고 있지 않고 **props로 받아서 화면에 그리기만** 하고, 사용자 입력은 **emit으로 부모에 올려서** 부모가 상태를 바꾸게 한다.
- `WeatherCard`는 카드 클릭(`select-card`)과 상세보기 클릭(`click-detail`)이 겹치지 않도록 `@click.stop`을 **컴포넌트 내부**에서 처리한다.

### `<slot>` — 시각적 위치와 스크립트 스코프는 다르다

`BaseDashboardCard`는 자기 안에 뭐가 들어올지 전혀 모른 채 `<slot />` 자리만 마련해두고, 실제 내용(`SearchBar`, `WeatherCard`)은 `WeatherParent`가 채워 넣는다. 시각적으로는 `BaseDashboardCard` 내부에 있는 것처럼 보이지만, `:search-query="searchQuery"`처럼 바인딩되는 값은 `BaseDashboardCard`가 아니라 **slot을 채운 쪽(WeatherParent)의 스코프**에서 평가된다 — 그래서 `BaseDashboardCard`를 건드리지 않고도 `WeatherParent`와 `SearchBar`/`WeatherCard`가 직접 데이터를 주고받을 수 있다.

---

## 💡 본인 추가 구현

- 과제 2까지의 검색 필터링(`computed`), `watch`/`watchEffect` 로그, 배경 전환 로직은 전부 `WeatherParent.vue`에 그대로 유지한 채 화면만 4개 컴포넌트로 쪼갰다.
- 컴포넌트별 스타일은 각자의 `<style scoped>`로 분리해서, 어떤 스타일이 어떤 컴포넌트 소속인지 명확하게 구분했다.
