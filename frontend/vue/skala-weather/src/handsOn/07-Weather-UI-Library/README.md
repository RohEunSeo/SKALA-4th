# 과제 7 — Weather UI Library (Element Plus 연동)

## 📌 과제 개요

지금까지는 검색창, 버튼, 숫자 표시까지 전부 순수 HTML 태그 + 커스텀 CSS(글래스모피즘)로 직접 만들어 왔다.
이번 과제는 그 위에 **Element Plus**라는 Vue 3 전용 UI 컴포넌트 라이브러리를 얹어서,
"디자인은 그대로 유지하면서 기능성 컴포넌트(로딩, 빈 상태, 에러 알림, 통계 표시)만 라이브러리로 교체"하는 연습이다.

### 왜 Element Plus인가

| 후보 | 선택 여부 | 이유 |
|---|---|---|
| **Element Plus** | ✅ 선택 | Vue 3 전용(Composition API 친화적), `<el-skeleton>`/`<el-statistic>`/`<el-descriptions>`처럼 이번 과제에 필요한 컴포넌트가 기본 내장, `:deep()`으로 내부 스타일을 뚫기 쉬운 BEM 클래스 구조 |
| Vuetify | ❌ | Material Design 색/입체감이 강해서 기존 글래스모피즘 톤을 지우고 다시 칠해야 함 |
| Naive UI | ❌ | 가볍고 커스터마이징은 쉽지만 `el-statistic`, `el-descriptions`에 대응하는 컴포넌트 이름/사용법이 낯설어 학습 곡선이 더 큼 |

---

## 📁 추가/수정 파일 목록

```
package.json / package-lock.json   ← 수정: element-plus, @element-plus/icons-vue 의존성 추가
src/
├── main.js                        ← 수정: ElementPlus 플러그인 + 아이콘 컴포넌트 전역 등록
├── App.vue                        ← 수정: API 에러 시 el-alert 전역 배너
├── components/
│   └── UnitToggler.vue            ← 수정: 단위변경 버튼 → el-switch
└── views/
    ├── WeatherHomeView.vue        ← 수정: el-input, el-skeleton, el-empty, el-alert
    ├── WeatherDetailView.vue      ← 수정: el-descriptions, el-button
    └── WeatherStatsView.vue       ← 수정: el-statistic
```

---

## 🧩 적용한 컴포넌트 목록

| 컴포넌트 | 적용 위치 | 대체한 요소 |
|---|---|---|
| `el-input` + `el-icon`(`Search`) | `WeatherHomeView.vue` 도시 검색창 | `<input type="text">` |
| `el-skeleton` | `WeatherHomeView.vue` 날씨 카드 영역 | 없음(신규) — `isLoading` 중 카드 자리 표시 |
| `el-empty` | `WeatherHomeView.vue` 검색 결과 없음 | `<p>😢 검색 결과가 일치하는 도시가 없습니다.</p>` |
| `el-alert` | `WeatherHomeView.vue`, `App.vue` | 없음(신규) — API 에러 시 표시 |
| `el-descriptions` / `el-descriptions-item` | `WeatherDetailView.vue` 상세 기상 정보 | `.info-grid` 6칸 커스텀 그리드 |
| `el-button` (`type="primary" round`) | `WeatherDetailView.vue` 하단 버튼 | `<button>← 메인 대시보드로 이동</button>` |
| `el-statistic` | `WeatherStatsView.vue` 4개 숫자 통계 | 평균기온/더운 도시 수/선선한 도시 수/전체 도시 수의 `<span class="stat-value">` |
| `el-switch` | `UnitToggler.vue` 단위 전환 | `<button>단위변경</button>` |

> **그대로 유지한 것**: `WeatherStatsView.vue`의 "가장 더운 도시" / "가장 선선한 도시"는 숫자 하나가 아니라 "도시명 + 온도" 텍스트 조합이라 `el-statistic`의 단일 숫자 포맷과 안 맞아서 기존 `<span>` 텍스트를 그대로 뒀다. `WeatherHomeView.vue`의 상세보기 슬라이드 패널, 배경 전환, 검색 필터링, 단위 변환 로직은 과제 범위에 없어 전혀 건드리지 않았다.

---

## 🔄 적용 전 / 후 비교

### 1. 도시 검색창

- **Before**: `<input>` + 직접 만든 `.search-input` CSS (padding/border/radius 전부 수동 지정)
- **After**: `<el-input>`에 `#prefix` 슬롯으로 돋보기 아이콘을 얹고, 스타일은 `.el-input__wrapper`/`.el-input__inner`를 `:deep()`으로 오버라이드해서 기존과 똑같은 반투명 유리 느낌 유지. `:value` + `@input="(val) => searchQuery = val"` 바인딩 방식은 그대로.

### 2. 로딩 상태

- **Before**: 로딩 상태를 별도로 표시하지 않음(과제 6에서 오히려 로딩 문구를 없앴었음)
- **After**: `isLoading`이 true인 동안 카드 자리에 `<el-skeleton :rows="4" animated />`가 뜨고, 데이터가 오면 카드로 교체됨. "빈 화면"이 아니라 "곧 채워질 자리"라는 걸 시각적으로 알려줌.

### 3. 검색 결과 없음

- **Before**: 텍스트 한 줄(`😢 검색 결과가 일치하는 도시가 없습니다.`)
- **After**: `<el-empty description="검색 결과가 없습니다." />` — 일러스트 아이콘이 붙어서 "찾는 게 없다"는 느낌이 더 명확해짐.

### 4. 상세 페이지 기상 정보

- **Before**: `.info-grid`(2열 grid) + `.info-item` 6개를 손으로 배치
- **After**: `<el-descriptions :column="2" border>` 한 번으로 같은 2열 테이블 레이아웃을 만들고, 셀 배경/테두리/글자색만 `:deep()`으로 기존 글래스 카드 톤에 맞춰 다시 칠함.

### 5. 통계 숫자

- **Before**: `{{ avgTemp }}°C` 같은 순수 텍스트 보간
- **After**: `<el-statistic :value="Number(avgTemp)" :precision="1" suffix="°C" />` — 값과 단위(°C, 개)가 컴포넌트 레벨에서 분리되고, 🔥/❄️ 아이콘은 `#prefix` 슬롯으로 배치.

### 6. 단위 전환 버튼

- **Before**: `<button @click="toggleUnit">단위변경</button>`
- **After**: `<el-switch active-text="°F" inactive-text="°C" @change="toggleUnit" />` — 클릭이 아니라 켜짐/꺼짐이 시각적으로 바로 보이는 토글 형태로 바뀜. `toggleUnit` 액션은 그대로 재사용(현재 `unit` 값을 스토어가 알아서 뒤집으므로, 스위치는 `unit === 'fahrenheit'` 여부만 보여주고 클릭 시 액션만 호출).

### 7. 에러 처리

- **Before**: `weatherStore.error`를 세팅만 하고 화면에 표시하는 곳이 없었음
- **After**: `WeatherHomeView.vue`(현재 페이지 컨텍스트)와 `App.vue`(어느 페이지에 있든 보이는 전역 배너) 두 곳에 `<el-alert type="error" show-icon />`을 배치해서 API 실패를 놓치지 않게 함.

---

## 🎨 기존 디자인을 깨지 않기 위해 한 것

Element Plus 컴포넌트는 기본적으로 흰 배경 + 그림자 + 브랜드 블루 색상을 깔고 나오기 때문에, 그대로 쓰면 기존 반투명 유리 카드 위에 "불투명한 흰 박스"가 얹히는 모양이 된다. 그래서 각 파일의 `<style scoped>`에 새 규칙만 **추가**하고(기존 규칙은 건드리지 않고), Element 내부 DOM에는 `:deep()`으로 접근했다.

```css
/* WeatherHomeView.vue — el-input을 기존 --input-bg 변수 톤에 맞춤 */
.search-input :deep(.el-input__wrapper) {
  background: var(--input-bg);
  border: 1px solid var(--input-border);
  box-shadow: none; /* Element 기본 그림자 제거 */
}
```

```css
/* WeatherDetailView.vue — back-btn이 el-button(primary/round)이 되면서
   Element 기본 파란색을 기존 반투명 화이트 톤으로 덮어씀.
   클래스 2개를 조합해 특이도를 높여서 Element 기본 스타일보다 우선 적용되게 함 */
.back-btn.el-button {
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  color: white;
}
```

---

## 💡 배운 점

1. **기존 CSS 변수 재사용이 곧 "라이브러리 티가 안 나게" 만드는 지름길이었다.** `--input-bg`, `--text-color`처럼 이미 라이트/다크 테마에 따라 값이 바뀌는 변수를 Element 컴포넌트 오버라이드에도 그대로 꽂아 넣으니, 배경 전환(맑음/흐림 등) 애니메이션이 el-input/el-statistic에도 자동으로 적용됐다. 색을 새로 하드코딩했다면 테마가 바뀔 때 라이브러리 컴포넌트만 안 따라오는 불일치가 생겼을 것이다.
2. **`:deep()` 없이는 컴포넌트 내부에 손을 댈 수 없다.** Vue의 scoped CSS는 컴포넌트 루트 엘리먼트까지만 셀렉터가 먹기 때문에, `el-input`처럼 내부에 여러 겹 DOM(`.el-input__wrapper` → `.el-input__inner`)을 감싸는 컴포넌트는 `:deep()`을 안 쓰면 아예 스타일이 안 먹는다. 반면 `el-button`처럼 클래스가 루트 엘리먼트에 직접 붙는 컴포넌트는 `:deep()` 없이 일반 클래스 선택자로도 되지만, 대신 **특이도(specificity) 싸움**에서 지지 않도록 클래스를 2개 이상 조합해야 했다(`.back-btn.el-button`).
3. **아이콘/컴포넌트를 전역 등록해두면 각 파일이 훨씬 가벼워진다.** `main.js`에서 `app.use(ElementPlus)`와 아이콘 전체를 한 번에 등록해두니, 이후 각 view 파일에서는 `import { Search } from '@element-plus/icons-vue'` 같은 걸 반복할 필요 없이 템플릿에 `<Search />`만 쓰면 됐다. 다만 이 방식은 실제로 안 쓰는 아이콘까지 번들에 포함되므로, 프로덕션이라면 필요한 아이콘만 개별 등록하는 게 맞다는 점도 같이 이해했다.
4. **컴포넌트가 필요로 하는 데이터 타입까지 신경 써야 한다.** `avgTemp`는 기존 로직에서 `toFixed(1)`로 이미 문자열(`"28.5"`)이 되어 있었는데, `el-statistic`의 `value`는 숫자를 기대해서 `Number(avgTemp)` + `:precision="1"`로 다시 맞춰줘야 했다. 기존 `computed`(avgTemp 자체)는 손대지 않고, "화면에 넘길 때만" 타입을 변환하는 선에서 해결한 것이 포인트.
