<script setup>
import { ref, computed, watch, watchEffect } from 'vue'
import BaseDashboardCard from './BaseDashboardCard.vue'
import SearchBar from './SearchBar.vue'
import WeatherCard from './WeatherCard.vue'

// 모든 반응형 데이터는 부모가 관리 — 자식은 props로 받고 emit으로 올릴 뿐
const weatherList = ref([
  { id: 'city_01', name: '서울', temp: 28, status: '맑음' },
  { id: 'city_02', name: '수원', temp: 24, status: '비' },
  { id: 'city_03', name: '부산', temp: 26, status: '구름' },
  { id: 'city_04', name: '대전', temp: 23, status: '맑음' },
  { id: 'city_05', name: '제주', temp: 30, status: '흐림' },
])

const searchQuery = ref('')
const selectedCityInfo = ref(null)

const bgMap = {
  '맑음': 'linear-gradient(160deg, #f7971e, #ffd200, #a8e063, #56ccf2)',
  '비': 'linear-gradient(160deg, #2c3e50, #3498db, #4a6fa5)',
  '구름': 'linear-gradient(160deg, #757f9a, #d7dde8, #a8c0cc)',
  '흐림': 'linear-gradient(160deg, #606c88, #3f4c6b, #4b5d67)',
}
const currentBg = ref(bgMap['맑음'])
const nextBg = ref(bgMap['맑음'])
const isFading = ref(false)

// computed — 검색어 기반 필터링
const filteredWeatherList = computed(() => {
  if (!searchQuery.value) return weatherList.value
  return weatherList.value.filter((city) => city.name.includes(searchQuery.value))
})

// watch — selectedCityInfo가 바뀔 때마다 콘솔 로그
watch(selectedCityInfo, (newVal) => {
  if (newVal) {
    console.log(`[watch] 선택된 도시 변경 → ${newVal.name} (${newVal.status}, ${newVal.temp}°C)`)
  }
})

// watchEffect — searchQuery 자동 추적
watchEffect(() => {
  console.log(`[watchEffect] 현재 검색어: "${searchQuery.value}"`)
})

// SearchBar에서 update-query 이벤트 받아서 searchQuery 업데이트
const handleUpdateQuery = (query) => {
  searchQuery.value = query
}

// WeatherCard에서 select-card 이벤트 받아서 도시 선택 + 배경 전환
const handleSelectCard = (city) => {
  selectedCityInfo.value = city
  const newBg = bgMap[city.status] ?? bgMap['맑음']
  nextBg.value = newBg
  isFading.value = true
  setTimeout(() => {
    currentBg.value = newBg
    isFading.value = false
  }, 800)
}

// WeatherCard에서 click-detail 이벤트 받아서 alert
const handleClickDetail = (city) => {
  window.alert(`${city.name}의 현재 날씨는 [${city.status}] 상태입니다.`)
}
</script>

<template>
  <div class="wrapper">
    <div class="bg-layer next" :style="{ background: nextBg }"></div>
    <div class="bg-layer current" :class="{ fading: isFading }" :style="{ background: currentBg }"></div>

    <div class="container">
      <h1>🌤️ 과제 3: 날씨 (Component)</h1>

      <!-- BaseDashboardCard 껍데기 안에 SearchBar를 slot으로 꽂아 넣음 -->
      <BaseDashboardCard>
        <!-- SearchBar는 부모(WeatherParent)에서 바인딩 — Slot 참고사항 6번 -->
        <SearchBar
          :search-query="searchQuery"
          @update-query="handleUpdateQuery"
        />
      </BaseDashboardCard>

      <!-- BaseDashboardCard 껍데기 안에 날씨 목록을 slot으로 꽂아 넣음 -->
      <BaseDashboardCard>
        <h2 class="section-title">📋 지역별 날씨 현황</h2>

        <!-- 검색 결과 없을 때 안내 -->
        <p v-if="searchQuery && filteredWeatherList.length === 0" class="no-result">
          😢 검색 결과가 일치하는 도시가 없습니다.
        </p>

        <!-- WeatherCard를 v-for로 반복 — 각 카드에 city 데이터를 props로 내려줌 -->
        <WeatherCard
          v-for="city in filteredWeatherList"
          :key="city.id"
          :city="city"
          @select-card="handleSelectCard"
          @click-detail="handleClickDetail"
        />
      </BaseDashboardCard>

      <!-- 하단 상태바 -->
      <div class="status-section">
        <p v-if="selectedCityInfo">{{ selectedCityInfo.name }}이(가) 선택되었습니다.</p>
        <p v-else>카드를 클릭하거나 검색해 보세요.</p>
      </div>
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

.bg-layer.next { z-index: 0; }

.bg-layer.current {
  z-index: 1;
  opacity: 1;
  transition: opacity 0.8s ease;
}

.bg-layer.current.fading { opacity: 0; }

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

.section-title {
  color: white;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.no-result {
  text-align: center;
  color: white;
  font-size: 15px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.18);
  backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 20px;
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
}
</style>