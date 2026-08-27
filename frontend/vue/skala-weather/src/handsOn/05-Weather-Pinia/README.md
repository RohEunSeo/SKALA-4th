# 과제 5 — Weather Store (Pinia 적용)

## 📌 과제 개요

Weather Router(과제 4)를 기반으로 Pinia Store를 도입하여  
날씨 단위(°C / °F) 전환 기능을 전역 상태로 관리하는 과제.  
컴포넌트 간 Props 전달 없이 Store 하나로 앱 전체 상태를 제어하는 것이 핵심.

---

## 📁 추가/수정 파일 목록

src/
├── stores/
│ └── configStore.js ← 신규: 날씨 단위 전역 관리 Store
├── components/
│ └── UnitToggler.vue ← 신규: 단위 전환 UI 컴포넌트
├── views/
│ ├── WeatherHomeView.vue ← 수정: 온도 단위 변환 computed 적용
│ └── WeatherDetailView.vue ← 수정: 온도 단위 변환 computed 적용
└── App.vue ← 수정: NavBar에 UnitToggler 배치


---

## 🔑 핵심 개념 — 수업에서 배운 내용 활용

### Pinia Store 구성 3요소

| 구성요소 | Vue 3 매핑 | 이번 과제에서 한 것 |
|---------|-----------|-----------------|
| **state** | `ref()` | `unit` — 현재 날씨 단위 저장 (초기값: `'celsius'`) |
| **getters** | `computed()` | `unitSymbol` — unit에 따라 `'°C'` 또는 `'°F'` 반환 |
| **actions** | `function()` | `toggleUnit` — celsius ↔ fahrenheit 스위칭 |

### configStore가 해결한 것

단위 변경 버튼 하나로 **앱 전체 온도**가 동시에 바뀌어야 함.  
Props로 전달하면 WeatherParent → WeatherCard 계층을 모두 거쳐야 하고  
페이지가 달라지면 공유 자체가 불가능함.  
Pinia Store에 `unit`을 두면 어느 컴포넌트에서든 직접 접근 가능.

### storeToRefs 사용

state와 getters는 구조 분해 할당 시 반응성이 끊어지므로  
`storeToRefs()`로 감싸서 꺼냄.  
actions(함수)는 일반 구조 분해 할당으로 꺼냄.

```js
import { storeToRefs } from 'pinia'

const configStore = useConfigStore()
const { unit, unitSymbol } = storeToRefs(configStore)  // state, getters
const { toggleUnit } = configStore                      // actions
```

### 온도 단위 변환 공식

```js
// 원본 데이터는 항상 섭씨(°C)로 저장
// 화씨 변환: (섭씨 × 9 / 5) + 32
const displayTemp = computed(() => {
  if (configStore.unit === 'fahrenheit') {
    return Math.round((rawTemp * 9) / 5 + 32)
  }
  return rawTemp
})
```

---

## ✅ 과제 요구사항 구현 결과

### 1. configStore.js 작성

- `state`: `unit` (초기값: `'celsius'`)
- `getters`: `unitSymbol` — 현재 단위에 맞는 기호 반환
- `actions`: `toggleUnit` — 섭씨↔화씨 전환

### 2. UnitToggler.vue + NavBar 배치

- NavBar 우측에 현재 단위와 "단위변경" 버튼 표시
- 버튼 클릭 시 `configStore.toggleUnit()` 호출

### 3. 메인/상세 페이지 온도 변환 적용

- `WeatherHomeView`, `WeatherDetailView` 모두 `displayTemp` computed 적용
- 단위변경 버튼 클릭 즉시 모든 카드의 온도가 실시간으로 전환

### 4. 본인 추가 구현

- `configStore`에 `theme` state 추가 (라이트/다크 모드 전환)
  - `state`: `theme` (초기값: `'light'`)
  - `getters`: `isDark` — 현재 다크 모드 여부 반환
  - `actions`: `toggleTheme` — 테마 전환
- NavBar에 🌙/☀️ 테마 전환 버튼 추가

---

## 💡 배운 점

- Pinia Store는 Props/Emits 없이 어느 컴포넌트에서든 전역 데이터에 접근할 수 있어서,  
  페이지를 넘나드는 공유 상태 관리에 적합함을 직접 체감함
- `storeToRefs()`를 쓰지 않으면 구조 분해 할당 시 반응성이 끊어져서  
  화면이 갱신되지 않는 버그를 직접 확인하고 해결함
- computed를 활용한 온도 변환은 원본 데이터를 건드리지 않고  
  표시 단계에서만 가공하는 올바른 패턴임을 이해함