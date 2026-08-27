# 과제 1 — Weather Mockup

## 📌 과제 개요

Vue 3의 기본 템플릿 문법(`v-for`, `v-if`, 이벤트 바인딩)만으로 날씨 대시보드의 첫 화면(Mockup)을 만드는 과제. 아직 반응형 API(`computed`, `watch`)나 컴포넌트 분리는 다루지 않고, `ref()`로 만든 배열 하나를 화면에 그대로 그려내는 데 집중했다.

---

## ✅ 과제 요구사항 구현

| 요구사항 | 구현 위치 | 핵심 코드 |
|---|---|---|
| **v-for** — 배열 반복 렌더링 | 지역별 날씨 카드 목록 | `v-for="city in weatherList" :key="city.id"` |
| **v-if / v-else** — 조건부 라벨 | 카드 안 더움/선선함 라벨 | 25도 기준으로 분기 |
| **:value + @input** — v-model 내부 동작 이해 | 도시 검색창 | 아래 참고 |
| **@click + .stop** — 이벤트 버블링 차단 | 카드 클릭 vs 상세보기 버튼 | 아래 참고 |
| **본인 데이터 추가** | `weatherList` | 대전, 제주 2개 도시 추가 |

### v-for + v-if

```html
<div v-for="city in weatherList" :key="city.id" class="weather-card" @click="selectCity(city)">
  <p class="city-name">{{ city.name }} ({{ city.status }})</p>
  <p class="city-temp">현재 기온: {{ city.temp }}°C</p>
  <span v-if="city.temp >= 25" class="label hot">🔥 더움 (25도 이상)</span>
  <span v-else class="label cool">❄️ 선선함 (25도 미만)</span>
</div>
```

### :value + @input — v-model이 내부적으로 하는 일을 직접 구현

`v-model="searchCity"`를 그대로 쓰는 대신, 그게 내부적으로 어떤 prop/이벤트로 풀리는지 이해하려고 `:value` + `@input`으로 직접 작성했다.

```html
<input
  type="text"
  :value="searchCity"
  @input="(e) => (searchCity = e.target.value)"
  placeholder="검색할 도시 이름 입력"
/>
```

### @click.stop — 카드 클릭과 버튼 클릭 분리

카드 전체에는 "선택" 클릭 이벤트가, 버튼에는 "상세보기" 클릭 이벤트가 걸려 있어서 그대로 두면 버튼을 눌러도 카드 클릭 이벤트까지 같이 발생한다(이벤트 버블링). `.stop` 수식어로 버튼 클릭이 부모(카드)까지 전파되지 않게 막았다.

```html
<div class="weather-card" @click="selectCity(city)">
  ...
  <button @click.stop="showDetail(city.name, city.status)">상세보기</button>
</div>
```

---

## 💡 본인 추가 구현

- **데이터 추가**: 기본 3개 도시(서울/수원/부산)에 대전·제주를 추가해서 5개 도시로 구성
- **날씨 상태별 배경 그라디언트 전환**: 카드 클릭 시 `status`(맑음/비/구름/흐림)에 맞는 배경으로 0.8초에 걸쳐 부드럽게 페이드 전환되도록 2장의 배경 레이어(`current`/`next`)를 겹쳐서 구현
- **글래스모피즘 UI 스타일**: `backdrop-filter: blur()` + 반투명 배경으로 카드/섹션 전체를 유리 재질처럼 표현
