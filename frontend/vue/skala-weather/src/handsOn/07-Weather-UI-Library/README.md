# 과제 7 — Weather UI Library (외부 UI 라이브러리 자유 적용)

## 📌 과제 개요

기존에는 검색창, 버튼, 숫자 표시, 날씨 아이콘, 국기까지 전부 순수 HTML 태그 + 커스텀 CSS(글래스모피즘)로 직접 만들어 왔다.
이번 과제는 그 위에 여러 **외부 UI/아이콘 라이브러리**를 자유롭게 골라 얹어서, "디자인은 그대로 유지하면서 기능성·표현 요소만 라이브러리로 교체"하는 연습이다.

> API 관련 기능 확장(세계 도시 검색, 5일 예보)은 axios/OpenWeatherAPI 쪽 내용이라 [06-Weather-Axios](../06-Weather-Axios/README.md)에서 다룬다. 이 문서는 순수하게 "어떤 라이브러리를 왜, 어디에 적용했는지"만 다룬다.

### 왜 이 3개 라이브러리인가

| 라이브러리 | 용도 | 선택 이유 |
|---|---|---|
| **Element Plus** | 검색창(세계 도시 검색), 버튼, 알림, 로딩, 통계 표시 등 전반적인 UI 컴포넌트 | Vue 3 전용(Composition API 친화적), 이번 프로젝트에 필요한 컴포넌트(`el-skeleton`/`el-statistic`/`el-descriptions`/`el-alert` 등)가 기본 내장, `:deep()`으로 내부 스타일을 뚫기 쉬운 BEM 클래스 구조 |
| **weather-icons** | 날씨 상태 아이콘 | 날씨 전용 아이콘 폰트. `<i class="wi wi-day-sunny">`처럼 클래스 하나로 끝나서 기존 텍스트(`판교 (흐림)`) 옆에 자연스럽게 붙일 수 있고, 색상은 `currentColor`를 따라가서 라이트/다크 배경 전환에도 별도 처리 없이 잘 맞음 |
| **flag-icons** | 세계 도시 검색 결과의 국기 배경 | 국가 코드(`kr`, `cn`, `jp`...) 클래스 하나로 해당 국기 SVG를 보여줌. OpenWeather 응답의 `sys.country` 값과 그대로 매핑되고, sprite가 아니라 국가별 개별 SVG라 카드 배경으로 확대/배치하기 쉬움 |

**보류한 후보**: tsparticles로 "비 오면 배경에 빗방울이 떨어지는" 애니메이션 연출도 고려했지만, 구현량이 크고 성능 튜닝이 필요해서 이번 범위(라이브러리로 표현 요소 하나씩 개선)에는 과하다고 판단해 다음 과제 후보로 남겨뒀다.

**의도적으로 라이브러리를 안 쓴 곳**: 한국 도시 검색창은 `el-input` 대신 네이티브 `<input>`을 쓴다. `el-input`이 한글 조합(IME) 입력 중 값이 간헐적으로 씹히는 문제가 있어서, 한글 입력이 발생하는 이 검색창만 브라우저 표준 v-model로 되돌렸다(세계 도시 검색창은 영문만 입력해서 이 문제가 없어 `el-input`을 그대로 씀). 라이브러리를 "무조건 전부"가 아니라 "안전하게 쓸 수 있는 곳"에 적용하는 것도 중요한 판단이라고 생각한다.

---

## 📁 추가/수정 파일 목록

```
package.json / package-lock.json     ← 수정: element-plus, weather-icons, flag-icons 의존성 추가
src/
├── main.js                          ← 수정: ElementPlus 플러그인 + 아이콘 전역 등록, weather-icons/flag-icons CSS import
├── components/
│   └── UnitToggler.vue              ← 수정: 단위변경 버튼 → el-switch
└── views/
    ├── WeatherHomeView.vue          ← 수정: el-input(세계 검색)/el-skeleton/el-empty/el-alert/el-radio-group,
    │                                        weather-icons 아이콘, flag-icons 국기 배경
    ├── WeatherDetailView.vue        ← 수정: el-descriptions, el-button, weather-icons 아이콘
    └── WeatherStatsView.vue         ← 수정: el-statistic
```

---

## 🧩 1. Element Plus — 기본 UI 컴포넌트

| 컴포넌트 | 적용 위치 | 대체한 요소 |
|---|---|---|
| `el-input` + `el-icon`(`Search`) | 세계 도시 검색창 | `<input type="text">` |
| `el-skeleton` | 날씨 카드 영역, 5일 예보 로딩 | 없음(신규) — 로딩 중 자리 표시 |
| `el-empty` | 검색 결과 없음 | `<p>😢 검색 결과가 일치하는 도시가 없습니다.</p>` |
| `el-alert` | API 에러 표시 (`WeatherHomeView`, `App.vue`, 세계 도시 검색 에러) | 없음(신규) |
| `el-descriptions` / `el-descriptions-item` | 상세 기상 정보 | `.info-grid` 6칸 커스텀 그리드 |
| `el-button` (`type="primary" round`) | 뒤로가기/검색 버튼 | `<button>` |
| `el-statistic` | 날씨 통계(평균기온, 더운/선선한 도시 수, 전체 도시 수) | `<span class="stat-value">` |
| `el-switch` | 단위 전환 | `<button>단위변경</button>` |
| `el-radio-group` / `el-radio-button` | 한국/세계 검색 토글 | 없음(신규) |

### Element Plus 적용 전 / 후

| | Before | After |
|---|---|---|
| 세계 도시 검색창 | `<input>` + 직접 만든 `.search-input` CSS | `<el-input>` — `.el-input__wrapper`/`.el-input__inner`를 `:deep()`으로 오버라이드해서 기존과 동일한 반투명 유리 느낌 유지 |
| 로딩 상태 | 표시 안 함 | `isLoading`일 때 `<el-skeleton>`으로 카드 자리 표시 |
| 검색 결과 없음 | 텍스트 한 줄 | `<el-empty>` — 일러스트 아이콘 포함 |
| 상세 정보 | `.info-grid`(2열 grid) 수작업 배치 | `<el-descriptions :column="2" border>` |
| 통계 숫자 | `{{ avgTemp }}°C` 텍스트 보간 | `<el-statistic :value :precision suffix>` |
| 단위 전환 | `<button>단위변경</button>` | `<el-switch active-text="°F" inactive-text="°C">` |

### 기존 디자인을 깨지 않기 위해 한 것

Element Plus 컴포넌트는 기본적으로 흰 배경 + 그림자 + 브랜드 블루 색상을 깔고 나오기 때문에, 그대로 쓰면 기존 반투명 유리 카드 위에 "불투명한 흰 박스"가 얹히는 모양이 된다. 그래서 각 파일의 `<style scoped>`에 새 규칙만 **추가**하고(기존 규칙은 건드리지 않고), Element 내부 DOM에는 `:deep()`으로 접근했다. 이미 라이트/다크 테마에 따라 값이 바뀌는 CSS 변수(`--input-bg`, `--text-color` 등)를 그대로 재사용해서, 배경 전환 애니메이션이 Element 컴포넌트에도 자동으로 따라오게 했다.

```css
/* el-input을 기존 --input-bg 변수 톤에 맞춤 */
.search-input :deep(.el-input__wrapper) {
  background: var(--input-bg);
  border: 1px solid var(--input-border);
  box-shadow: none; /* Element 기본 그림자 제거 */
}
```

```css
/* el-button처럼 클래스가 루트 엘리먼트에 직접 붙는 컴포넌트는 :deep() 없이도 되지만,
   대신 특이도(specificity) 싸움에서 지지 않도록 클래스를 2개 조합 */
.back-btn.el-button {
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  color: white;
}
```

---

## 🧊 2. weather-icons — 날씨 상태 아이콘

API 원문 문구("온흐림", "약한 비" 등)를 그대로 노출하는 대신, **맑음/흐림/바람/비** 4종류로 재분류하고 각각에 아이콘을 붙였다.

```js
// src/stores/weatherStore.js
const classifyWeather = (id, windSpeed) => {
  if (windSpeed >= 8) return '바람'       // 초속 8m↑ — 체감상 뚜렷하게 바람 부는 날
  if (id >= 200 && id < 600) return '비'   // 뇌우, 이슬비, 비
  if (id >= 600 && id < 800) return '흐림' // 눈, 안개/황사 등 대기 현상
  if (id === 800 || id === 801) return '맑음'
  return '흐림'                            // 구름 많음~흐림
}

const ICON_CLASS_MAP = {
  '맑음': { day: 'wi-day-sunny', night: 'wi-night-clear' },
  '흐림': { day: 'wi-day-cloudy', night: 'wi-night-alt-cloudy' },
  '바람': { day: 'wi-strong-wind', night: 'wi-strong-wind' },
  '비': { day: 'wi-day-rain', night: 'wi-night-alt-rain' },
}
export const getWeatherIconClass = (city) => {
  const isDay = !city?.icon || city.icon.endsWith('d') // API의 icon 코드 마지막 글자(d/n)로 낮/밤 판단
  return ICON_CLASS_MAP[city?.condition]?.[isDay ? 'day' : 'night'] ?? 'wi-na'
}
```

`getWeatherIconClass`를 store에서 export해서 카드 목록/상세 패널/상세 페이지/5일 예보까지 전부 같은 함수로 아이콘을 고른다 — 로직이 여러 파일에 흩어지지 않게 했다.

문구도 "판교 (온흐림)" → "☁️ 판교 (흐림)"처럼 아이콘 + 단순화된 문구로 바뀌었고, 배경 전환도 같은 분류(`condition`)를 공유한다.

---

## 🚩 3. flag-icons — 세계 도시 검색 결과 국기 배경

세계 도시 검색 결과 카드에 해당 국가의 국기를 은은한 배경으로 깔았다.

```html
<div class="weather-card world-result-card">
  <span class="fi world-flag-bg" :class="'fi-' + worldCityResult.country.toLowerCase()"></span>
  <div class="card-info">...</div>
</div>
```

```css
.world-flag-bg {
  display: block;         /* .fi 기본값(inline-block)이라 inset만으론 안 늘어나서 명시적으로 덮어씀 */
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background-size: contain;   /* cover로 늘리면 국기 비율이 깨져서 색 블록처럼 보임 */
  background-position: right center; /* 왼쪽 텍스트와 안 겹치게 오른쪽에 배치 */
  background-repeat: no-repeat;
  opacity: 0.45;
}
```

**시행착오**: 처음엔 `background-size: cover`로 카드 전체를 채우려 했는데, 카드가 국기의 실제 비율(4:3)보다 훨씬 넓고 납작해서 국기가 심하게 늘어나 그냥 색깔 줄무늬처럼 보였다(예: 멕시코 국기가 초록/흰/빨강 블록으로만 보임). `background-size: contain`으로 원래 비율을 유지하고 오른쪽에 작게 배치하는 걸로 바꾸니 실제 국기 모양이 그대로 보였다.

---

## 🛠️ 곁다리로 고친 것 — 기능 없는 테마 토글 버튼 제거

네비게이션 바에 있던 🌙/☀️ 버튼(`configStore`의 `theme`/`isDark`/`toggleTheme`)이 실제로는 화면 어디에도 반영되지 않는 죽은 기능이었다(날씨 조건에 따른 라이트/다크 전환은 별도의 `isLightBg` 로직으로 이미 처리되고 있었음). 버튼과 관련 스타일, 그리고 store의 죽은 상태/액션까지 함께 정리했다.

---

## 💡 배운 점

1. **API 원문 텍스트를 그대로 노출하지 말고, 우리 서비스 기준으로 다시 분류하는 게 UX상 훨씬 낫다.** "온흐림", "튼구름" 같은 표현은 정확하지만 사용자 입장에선 판단하기 어렵다. 소수의 명확한 카테고리(맑음/흐림/바람/비)로 재분류하니 아이콘 매핑도 쉬워지고 배경 전환 로직과도 기준을 통일할 수 있었다.
2. **아이콘/이미지 라이브러리는 원본 비율을 함부로 늘리면 오히려 못 알아보게 된다.** `background-size: cover`로 국기를 억지로 채우려다 실패한 경험 — 라이브러리가 제공하는 에셋(국기, 아이콘)은 "원래 비율"이 곧 그 콘텐츠의 정체성이라, `contain`으로 비율을 지키는 게 화려함보다 인식 가능성에서 훨씬 중요했다.
3. **아이콘 폰트 라이브러리는 색상 상속(`currentColor`)이 되는지가 실사용 편의성을 크게 좌우한다.** weather-icons는 `<i>` 태그의 `color`를 그대로 따라가서, 기존 라이트/다크 텍스트 색상 전환(`var(--text-color)`) 로직에 아이콘도 자동으로 맞춰졌다.
4. **라이브러리는 "전부 다 쓰기"보다 "안전한 곳에 쓰기"가 맞다.** `el-input`을 한글이 들어가는 검색창에 썼다가 조합(IME) 입력이 깨지는 문제를 겪은 뒤, 그 부분만 네이티브 input으로 되돌렸다. 라이브러리 적용은 일관성도 중요하지만, 특정 조합(여기선 "Element Plus + 한글 IME")에서 실제로 버그가 있다면 예외를 두는 게 더 나은 선택일 수 있다는 걸 체감했다.
5. **죽은 기능은 눈에 보이는 UI가 있어도 실제로 뭘 바꾸는지 확인해야 한다.** 테마 토글 버튼은 클릭하면 반응은 있었지만(아이콘이 바뀜) 실제로 store 값만 뒤집을 뿐 화면 어디에도 연결이 안 되어 있었다 — grep으로 실제 소비처를 확인하고 나서야 죽은 코드라는 걸 확신할 수 있었다.
