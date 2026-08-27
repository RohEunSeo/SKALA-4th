# 과제 2 — Weather Composition

## 📌 과제 개요

과제 1(Mockup)의 정적인 화면에 Composition API의 반응형 도구(`computed`, `watch`, `watchEffect`)를 적용해서, 검색어에 따라 목록이 실시간으로 걸러지고 상태 변화가 자동으로 추적되는 화면으로 발전시키는 과제.

---

## ✅ 과제 요구사항 구현

| 요구사항 | 구현 위치 | 핵심 개념 |
|---|---|---|
| **ref()** | `searchQuery`, `selectedCityInfo`, `weatherList` | 반응형 상태 3가지 관리 |
| **computed()** | `filteredWeatherList` | 검색어 기반 도시 필터링 (의존값이 바뀔 때만 재계산) |
| **watch()** | `selectedCityInfo` 감시 | 값이 바뀔 때만 실행, 이전 로직과 분리해서 부수효과 처리 |
| **watchEffect()** | `searchQuery` 감시 | 등록 즉시 1회 실행 + 내부에서 참조한 값이 바뀔 때마다 자동 재실행 |
| **검색 결과 없음 안내** | 카드 목록 위 | 검색어는 있는데 결과가 0개일 때만 노출 |

### computed — 검색어로 목록 필터링

```js
const filteredWeatherList = computed(() => {
  if (!searchQuery.value) return weatherList.value
  return weatherList.value.filter((city) => city.name.includes(searchQuery.value))
})
```

### watch vs watchEffect — 같은 "감시"인데 쓰임이 다름

```js
// watch — 감시 대상(selectedCityInfo)을 명시적으로 지정, 값이 "바뀔 때만" 실행
watch(selectedCityInfo, (newVal) => {
  if (newVal) {
    console.log(`[watch] 선택된 도시 변경 → ${newVal.name} (${newVal.status}, ${newVal.temp}°C)`)
  }
})

// watchEffect — 감시 대상을 따로 안 적어도, 콜백 안에서 참조한 반응형 값(searchQuery)이
// 바뀔 때마다 자동으로 다시 실행됨. 등록되는 순간에도 1회 즉시 실행됨.
watchEffect(() => {
  console.log(`[watchEffect] 현재 검색어: "${searchQuery.value}"`)
})
```

### 검색 결과 없을 때 안내 문구

```html
<p v-if="searchQuery && filteredWeatherList.length === 0" class="no-result">
  😢 검색 결과가 일치하는 도시가 없습니다.
</p>
```

---

## 💡 본인 추가 구현

**`avgTemp` computed** — 현재 화면에 보이는(=검색어로 필터링된) 도시들의 평균 기온을 실시간으로 계산해서 표시.

```js
const avgTemp = computed(() => {
  if (filteredWeatherList.value.length === 0) return 0
  const total = filteredWeatherList.value.reduce((sum, city) => sum + city.temp, 0)
  return (total / filteredWeatherList.value.length).toFixed(1)
})
```

`filteredWeatherList`(검색 결과)를 기준으로 계산하기 때문에, 검색어를 입력해서 목록이 줄어들면 평균 기온도 그에 맞춰 자동으로 다시 계산된다 — `computed`가 의존하는 값(`filteredWeatherList`)이 바뀌면 별도 처리 없이 알아서 재평가되는 걸 직접 확인할 수 있었다.
