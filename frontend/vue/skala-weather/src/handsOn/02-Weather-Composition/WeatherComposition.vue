<script setup>
import { ref, computed, watch, watchEffect } from 'vue'

// ① 반응형 상태 관리
// 1일차랑 동일한 데이터 + searchQuery, selectedCityInfo 추가
const weatherList = ref([
  { id: 'city_01', name: '서울', temp: 28, status: '맑음' },
  { id: 'city_02', name: '수원', temp: 24, status: '비' },
  { id: 'city_03', name: '부산', temp: 26, status: '구름' },
  { id: 'city_04', name: '대전', temp: 23, status: '맑음' },
  { id: 'city_05', name: '제주', temp: 30, status: '흐림' },
])

// 검색창 입력값 — 1일차에서 searchCity였는데 과제 요구사항대로 searchQuery로 변경
const searchQuery = ref('')

// 카드 클릭 시 선택된 도시 객체 저장 — 1일차의 selectedCity(문자열)에서 객체로 변경
const selectedCityInfo = ref(null)

// 배경 전환 관련 — 1일차에서 가져옴
const bgMap = {
  '맑음': 'linear-gradient(160deg, #f7971e, #ffd200, #a8e063, #56ccf2)',
  '비': 'linear-gradient(160deg, #2c3e50, #3498db, #4a6fa5)',
  '구름': 'linear-gradient(160deg, #757f9a, #d7dde8, #a8c0cc)',
  '흐림': 'linear-gradient(160deg, #606c88, #3f4c6b, #4b5d67)',
}
const currentBg = ref(bgMap['맑음'])
const nextBg = ref(bgMap['맑음'])
const isFading = ref(false)

// ② computed — 검색어 기반 필터링
// searchQuery나 weatherList가 바뀔 때만 자동으로 재계산됨
// 검색어 없으면 전체 반환, 있으면 도시 이름에 검색어 포함된 것만 반환
const filteredWeatherList = computed(() => {
  if (!searchQuery.value) return weatherList.value
  return weatherList.value.filter((city) => city.name.includes(searchQuery.value))
})

// ⑤ 본인 추가 — 전체 도시 평균 기온 computed
// filteredWeatherList 기준으로 현재 보이는 카드들의 평균 기온 계산
const avgTemp = computed(() => {
  if (filteredWeatherList.value.length === 0) return 0
  const total = filteredWeatherList.value.reduce((sum, city) => sum + city.temp, 0)
  return (total / filteredWeatherList.value.length).toFixed(1)
})

// ③ watch — selectedCityInfo 감시
// 선택된 도시가 바뀔 때마다 콘솔 로그 출력
watch(selectedCityInfo, (newVal) => {
  if (newVal) {
    console.log(`[watch] 선택된 도시 변경 → ${newVal.name} (${newVal.status}, ${newVal.temp}°C)`)
  }
})

// ③ watchEffect — searchQuery 감시
// 컴포넌트 생성 시 즉시 1회 실행 + searchQuery 바뀔 때마다 자동 실행
watchEffect(() => {
  console.log(`[watchEffect] 현재 검색어: "${searchQuery.value}"`)
})

// 카드 클릭 시 도시 선택 + 배경 전환
const selectCity = (city) => {
  selectedCityInfo.value = city
  const newBg = bgMap[city.status] ?? bgMap['맑음']
  nextBg.value = newBg
  isFading.value = true
  setTimeout(() => {
    currentBg.value = newBg
    isFading.value = false
  }, 800)
}

// 상세보기 버튼 클릭 시 alert — .stop으로 버블링 차단
const showDetail = (cityName, status) => {
  window.alert(`${cityName}의 현재 날씨는 [${status}] 상태입니다.`)
}
</script>

<template>
  <div class="wrapper">
    <div class="bg-layer next" :style="{ background: nextBg }"></div>
    <div class="bg-layer current" :class="{ fading: isFading }" :style="{ background: currentBg }"></div>

    <div class="container">
      <h1>🌤️ 과제 2: 날씨 (Composition)</h1>

      <!-- 검색 영역 -->
      <!-- v-model 내부 동작 원리대로 :value + @input으로 직접 작성 -->
      <section class="search-section">
        <h2>🔍 도시 검색</h2>
        <input
          type="text"
          :value="searchQuery"
          @input="(e) => (searchQuery = e.target.value)"
          placeholder="검색할 도시 이름 입력"
          class="search-input"
        />
        <p v-if="searchQuery">검색 중인 도시: {{ searchQuery }}</p>
      </section>

      <!-- ⑤ 평균 기온 표시 영역 -->
      <section class="avg-section">
        <p>📊 현재 표시 중인 도시 평균 기온: <strong>{{ avgTemp }}°C</strong></p>
      </section>

      <!-- 날씨 카드 목록 -->
      <!-- filteredWeatherList로 교체 — computed가 알아서 필터링해줌 -->
      <section class="weather-section">
        <h2>📋 지역별 날씨 현황</h2>

        <!-- ④ 검색 결과 없을 때 안내 문구 -->
        <p v-if="searchQuery && filteredWeatherList.length === 0" class="no-result">
          😢 검색 결과가 일치하는 도시가 없습니다.
        </p>

        <div
          v-for="city in filteredWeatherList"
          :key="city.id"
          class="weather-card"
          @click="selectCity(city)"
        >
          <div class="card-info">
            <p class="city-name">{{ city.name }} ({{ city.status }})</p>
            <p class="city-temp">현재 기온: {{ city.temp }}°C</p>
            <span v-if="city.temp >= 25" class="label hot">🔥 더움 (25도 이상)</span>
            <span v-else class="label cool">❄️ 선선함 (25도 미만)</span>
          </div>
          <button @click.stop="showDetail(city.name, city.status)" class="detail-btn">
            상세보기
          </button>
        </div>
      </section>

      <!-- 선택된 도시 상태바 -->
      <section class="status-section">
        <p v-if="selectedCityInfo">{{ selectedCityInfo.name }}이(가) 선택되었습니다.</p>
        <p v-else>카드를 클릭하거나 검색해 보세요.</p>
      </section>
    </div>
  </div>
</template>

<style scoped>
.wrapper {
  position: relative;
  width: 100%;
  min-height: 100vh;
}

.bg-layer {
  position: fixed;
  inset: 0;
  width: 100%;
  height: 100%;
}

.bg-layer.next {
  z-index: 0;
}

.bg-layer.current {
  z-index: 1;
  opacity: 1;
  transition: opacity 0.8s ease;
}

.bg-layer.current.fading {
  opacity: 0;
}

.container {
  position: relative;
  z-index: 2;
  padding: 40px 20px;
  font-family: 'Apple SD Gothic Neo', sans-serif;
}

h1 {
  text-align: center;
  color: white;
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 36px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.search-section {
  max-width: 600px;
  margin: 0 auto 16px;
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  padding: 24px;
}

.search-section h2 {
  color: white;
  font-size: 16px;
  margin-bottom: 12px;
  font-weight: 600;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  font-size: 14px;
  background: rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  color: white;
  outline: none;
  box-sizing: border-box;
  transition: background 0.2s;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.search-input:focus {
  background: rgba(255, 255, 255, 0.4);
}

.search-section p {
  color: white;
  font-size: 13px;
  margin-top: 8px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

/* ⑤ 평균 기온 영역 */
.avg-section {
  max-width: 600px;
  margin: 0 auto 20px;
  text-align: center;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.18);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 14px;
  color: white;
  font-size: 14px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.avg-section strong {
  font-size: 18px;
  font-weight: 700;
}

.weather-section {
  max-width: 600px;
  margin: 0 auto 20px;
}

.weather-section h2 {
  color: white;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

/* ④ 검색 결과 없을 때 안내 문구 */
.no-result {
  text-align: center;
  color: white;
  font-size: 15px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.18);
  backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 20px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.weather-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  margin-bottom: 12px;
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  cursor: pointer;
  transition: background 0.2s, transform 0.15s;
}

.weather-card:hover {
  background: rgba(255, 255, 255, 0.38);
  transform: translateY(-2px);
}

.city-name {
  color: white;
  font-weight: 700;
  font-size: 16px;
  margin-bottom: 4px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.city-temp {
  color: rgba(255, 255, 255, 0.95);
  font-size: 13px;
  margin-bottom: 10px;
}

.label {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.hot {
  background: rgba(220, 80, 60, 0.85);
  color: white;
}

.cool {
  background: rgba(60, 140, 220, 0.85);
  color: white;
}

.detail-btn {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  color: white;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.2s;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.detail-btn:hover {
  background: rgba(255, 255, 255, 0.4);
}

.status-section {
  max-width: 600px;
  margin: 0 auto;
  text-align: center;
  padding: 16px;
  background: rgba(255, 255, 255, 0.22);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 16px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}
</style>