<script setup>
import { ref, computed, watch, watchEffect, onMounted } from 'vue'
// ✅ [과제 5 추가] configStore import
import { useConfigStore } from '@/stores/configStore'
// ✅ [과제 6 추가] weatherStore import
import { useWeatherStore, getWeatherIconClass } from '@/stores/weatherStore'
import { storeToRefs } from 'pinia'

// ✅ [과제 5 추가] configStore 인스턴스 + 반응형 추출
const configStore = useConfigStore()
const { unit, unitSymbol } = storeToRefs(configStore)

// ✅ [과제 6 추가] weatherStore 인스턴스 + 반응형 추출 (mockData 대체)
const weatherStore = useWeatherStore()
// ✅ [과제 7 추가] isLoading(스켈레톤), error(el-alert) 표시에 사용
// ✅ [과제 8 추가] 세계 도시 검색 결과/로딩/에러 + 5일 예보
const {
  weatherList,
  isLoading,
  error,
  worldCityResult,
  isWorldSearchLoading,
  worldSearchError,
  forecast,
  isForecastLoading,
} = storeToRefs(weatherStore)

const searchQuery = ref('')

// ✅ [과제 8 추가] 도시 검색 한국/세계 토글 — 기본은 한국
const searchMode = ref('domestic')
const searchSectionTitle = computed(() =>
  searchMode.value === 'domestic' ? '🇰🇷 한국 도시 검색' : '🌍 세계 도시 검색',
)
const searchModeEmoji = computed(() => (searchMode.value === 'domestic' ? '🇰🇷' : '🌍'))

// ✅ [과제 8 추가] 세계 도시 검색어 + 검색 실행
const worldQuery = ref('')
const handleWorldSearch = () => {
  weatherStore.searchWorldCity(worldQuery.value)
}

// ✅ [과제 8 추가] 5일 예보 날짜 표시용 포맷 ("2026-08-27" → "8.27(목)")
const formatForecastDate = (dateStr) => {
  const date = new Date(`${dateStr}T00:00:00`)
  const md = date.toLocaleDateString('ko-KR', { month: 'numeric', day: 'numeric' })
  const weekday = date.toLocaleDateString('ko-KR', { weekday: 'short' })
  return `${md}(${weekday})`
}

const bgMap = {
  '맑음': 'linear-gradient(160deg, #f7971e, #ffd200, #a8e063, #56ccf2)',
  '비': 'linear-gradient(160deg, #2c3e50, #3498db, #4a6fa5)',
  '바람': 'linear-gradient(160deg, #7ec8c3, #a8d8d0, #8fb8c4)',
  '흐림': 'linear-gradient(160deg, #6c8494, #9db4c4, #85a0b0)',
}

// ✅ 처음 진입했을 땐 아무 도시도 선택되지 않은 상태 — 배경은 항상 기본값(맑음)에서 시작
const selectedCityInfo = ref(null)
const currentBg = ref(bgMap['맑음'])
const nextBg = ref(bgMap['맑음'])
const isFading = ref(false)

// ✅ [과제 6 추가] 앱 진입 시 전체 도시 날씨 조회
onMounted(async () => {
  await weatherStore.fetchAllWeather()
})

// ✅ [과제 5 추가] 밝은 배경 상태 목록
const lightBgStatus = ['맑음', '바람']
const isLightBg = computed(() => {
  if (!selectedCityInfo.value) return true
  return lightBgStatus.includes(selectedCityInfo.value.condition)
})

// ✅ [과제 5 추가] CSS 변수 동적 바인딩
const themeStyle = computed(() => {
  if (isLightBg.value) {
    return {
      '--text-color': 'rgba(50, 50, 50, 0.9)',
      '--text-sub': 'rgba(50, 50, 50, 0.6)',
      '--input-bg': 'rgba(0, 0, 0, 0.08)',
      '--input-border': 'rgba(0, 0, 0, 0.2)',
      '--input-placeholder': 'rgba(0, 0, 0, 0.4)',
      '--card-bg': 'rgba(255, 255, 255, 0.45)',
      '--card-border': 'rgba(255, 255, 255, 0.7)',
      '--btn-color': 'rgba(50, 50, 50, 0.9)',
      '--btn-bg': 'rgba(0, 0, 0, 0.08)',
      '--btn-border': 'rgba(0, 0, 0, 0.15)',
    }
  }
  return {
    '--text-color': 'white',
    '--text-sub': 'rgba(255, 255, 255, 0.75)',
    '--input-bg': 'rgba(255, 255, 255, 0.3)',
    '--input-border': 'rgba(255, 255, 255, 0.5)',
    '--input-placeholder': 'rgba(255, 255, 255, 0.7)',
    '--card-bg': 'rgba(255, 255, 255, 0.28)',
    '--card-border': 'rgba(255, 255, 255, 0.5)',
    '--btn-color': 'white',
    '--btn-bg': 'rgba(255, 255, 255, 0.25)',
    '--btn-border': 'rgba(255, 255, 255, 0.5)',
  }
})

// computed — 검색어 기반 필터링
const filteredWeatherList = computed(() => {
  if (!searchQuery.value) return weatherList.value
  return weatherList.value.filter((city) => city.name.includes(searchQuery.value))
})

// ✅ [과제 5 추가] 온도 변환 함수 — 섭씨 원본을 현재 단위에 맞게 변환
const convertTemp = (rawTemp) => {
  if (unit.value === 'fahrenheit') {
    return Math.round((rawTemp * 9) / 5 + 32)
  }
  return rawTemp
}

// watch — selectedCityInfo 감시
watch(selectedCityInfo, (newVal) => {
  if (newVal) {
    console.log(`[watch] 선택된 도시 변경 → ${newVal.name} (${newVal.status}, ${newVal.temp}°C)`)
  }
})

// watchEffect — searchQuery 자동 추적
watchEffect(() => {
  console.log(`[watchEffect] 현재 검색어: "${searchQuery.value}"`)
})

// 카드 클릭 시 배경 변경
const selectCity = (city) => {
  selectedCityInfo.value = city
  const newBg = bgMap[city.condition] ?? bgMap['맑음']
  nextBg.value = newBg
  isFading.value = true
  setTimeout(() => {
    currentBg.value = newBg
    isFading.value = false
  }, 800)
}

// ✅ 상세보기 클릭 시 페이지 이동 대신 오른쪽 슬라이드 패널 표시
const showDetailPanel = ref(false)
const detailPanelCity = ref(null)

const goToDetail = (city) => {
  // ✅ 카드를 직접 클릭했을 때와 동일하게 선택 상태 + 배경도 함께 갱신
  selectCity(city)
  detailPanelCity.value = city
  showDetailPanel.value = true
  // ✅ [과제 8 추가] 상세 정보를 열 때마다 그 도시의 5일 예보 조회
  weatherStore.fetchForecast(city.id)
}

const closeDetailPanel = () => {
  showDetailPanel.value = false
}
</script>

<template>
  <div :style="themeStyle">
    <div class="bg-layer next" :style="{ background: nextBg }"></div>
    <div class="bg-layer current" :class="{ fading: isFading }" :style="{ background: currentBg }"></div>

    <div class="container">
      <h1 class="page-title">🌤️ 과제 7: UI 라이브러리 적용</h1>

      <!-- ✅ [과제 7 추가] API 에러 발생 시 표시 -->
      <el-alert
        v-if="error"
        title="날씨 정보를 불러오지 못했습니다. 잠시 후 다시 시도해 주세요."
        type="error"
        show-icon
        class="weather-alert"
      />

      <!-- 검색 영역 -->
      <section class="glass-card">
        <!-- ✅ [과제 8 수정] 토글 상태에 따라 제목이 "한국 도시 검색"/"세계 도시 검색"으로 바뀜 -->
        <h2 class="section-title">{{ searchSectionTitle }}</h2>

        <!-- ✅ [과제 8 추가] 한국/세계 토글 — 기본은 한국 -->
        <el-radio-group v-model="searchMode" class="search-mode-toggle">
          <el-radio-button label="domestic">한국</el-radio-button>
          <el-radio-button label="world">세계</el-radio-button>
        </el-radio-group>

        <!-- 한국 도시 검색: 등록된 6개 도시를 이름으로 필터링 -->
        <template v-if="searchMode === 'domestic'">
          <!-- ✅ el-input은 한글 조합(IME) 입력 시 간헐적으로 값이 씹히는 문제가 있어,
               한글 입력이 발생하는 이 검색창만 네이티브 input + v-model로 유지
               (Vue의 네이티브 v-model은 조합 입력을 브라우저 표준대로 정확히 처리함) -->
          <div class="native-search-wrap">
            <span class="search-mode-emoji">{{ searchModeEmoji }}</span>
            <input
              v-model="searchQuery"
              type="text"
              placeholder="검색할 도시 이름 입력"
              class="native-search-input"
            />
          </div>
          <p v-if="searchQuery" class="search-status">검색 중인 도시: {{ searchQuery }}</p>
        </template>

        <!-- ✅ [과제 8 추가] 세계 도시 검색: 입력한 영문 도시명을 실시간으로 API 조회 -->
        <template v-else>
          <div class="world-search-row">
            <el-input
              v-model="worldQuery"
              placeholder="영문 도시명 입력 (예: Tokyo, Paris, London)"
              class="search-input"
              @keyup.enter="handleWorldSearch"
            >
              <template #prefix>
                <span class="search-mode-emoji">{{ searchModeEmoji }}</span>
              </template>
            </el-input>
            <el-button
              type="primary"
              round
              :loading="isWorldSearchLoading"
              class="world-search-btn"
              @click="handleWorldSearch"
            >
              검색
            </el-button>
          </div>
          <p class="search-status world-search-hint">
            ※ 국가명이 아닌 실제 도시명을 입력해 주세요 (예: 'China' ✗ → 'Beijing' ✓)
          </p>

          <el-alert
            v-if="worldSearchError"
            title="도시를 찾을 수 없습니다. 영문 도시명을 확인해 주세요."
            type="error"
            show-icon
            class="weather-alert world-search-alert"
          />

          <!-- ✅ [과제 8 추가] flag-icons로 결과 카드 배경에 국기를 은은하게 표시 -->
          <div v-if="worldCityResult" class="weather-card world-result-card">
            <span
              v-if="worldCityResult.country"
              class="fi world-flag-bg"
              :class="'fi-' + worldCityResult.country.toLowerCase()"
            ></span>
            <div class="card-info">
              <p class="city-name">
                <i class="wi weather-icon" :class="getWeatherIconClass(worldCityResult)"></i>
                {{ worldCityResult.name }} ({{ worldCityResult.condition }})
              </p>
              <p class="city-temp">현재 기온: {{ convertTemp(worldCityResult.temp) }}{{ unitSymbol }}</p>
              <p class="city-humidity">습도: {{ worldCityResult.humidity }}%</p>
              <span v-if="worldCityResult.temp >= 25" class="label hot">🔥 더움</span>
              <span v-else class="label cool">❄️ 선선함</span>
            </div>
          </div>
        </template>
      </section>

      <!-- 날씨 카드 목록 — 한국 모드일 때만 -->
      <section v-if="searchMode === 'domestic'" class="weather-section">
        <h2 class="section-title">📋 지역별 날씨 현황</h2>

        <!-- 상태바 — 목록 바로 아래로 이동해서 선택 상태가 잘 보이도록 함 -->
        <div class="status-section">
          <p v-if="selectedCityInfo">{{ selectedCityInfo.name }}이(가) 선택되었습니다.</p>
          <p v-else>아직 선택한 지역이 없습니다. 아래 지역을 선택해주세요.</p>
        </div>

        <!-- ✅ [과제 7 추가] 로딩 중일 때 카드 자리에 스켈레톤 표시 -->
        <el-skeleton v-if="isLoading" :rows="4" animated class="weather-skeleton" />

        <!-- ✅ [과제 7 수정] 검색 결과 없음 → el-empty로 교체 -->
        <el-empty
          v-else-if="searchQuery && filteredWeatherList.length === 0"
          description="검색 결과가 없습니다."
          class="weather-empty"
        />

        <template v-else>
          <div
            v-for="city in filteredWeatherList"
            :key="city.id"
            class="weather-card"
            @click="selectCity(city)"
          >
            <div class="card-info">
              <p class="city-name">
                <i class="wi weather-icon" :class="getWeatherIconClass(city)"></i>
                {{ city.name }} ({{ city.condition }})
              </p>
              <!-- ✅ [과제 5 수정] convertTemp + unitSymbol로 단위 변환 표시 -->
              <p class="city-temp">현재 기온: {{ convertTemp(city.temp) }}{{ unitSymbol }}</p>
              <!-- ✅ 습도 표시 추가 -->
              <p class="city-humidity">습도: {{ city.humidity }}%</p>
              <span v-if="city.temp >= 25" class="label hot">🔥 더움</span>
              <span v-else class="label cool">❄️ 선선함</span>
            </div>
            <button @click.stop="goToDetail(city)" class="detail-btn">상세보기</button>
          </div>
        </template>
      </section>
    </div>

    <!-- ✅ 상세보기 — 페이지 이동 대신 오른쪽 슬라이드 패널로 표시 -->
    <!-- Teleport: main-content의 z-index 스택 안에 갇혀서 네비게이션 바에 가려지는 걸 방지 -->
    <Teleport to="body">
    <Transition name="panel-overlay-fade">
      <div v-if="showDetailPanel" class="detail-overlay" @click="closeDetailPanel"></div>
    </Transition>
    <Transition name="panel-slide">
      <aside v-if="showDetailPanel && detailPanelCity" class="detail-panel">
        <button class="panel-close" @click="closeDetailPanel">✕</button>
        <h2 class="panel-title">
          <i class="wi weather-icon" :class="getWeatherIconClass(detailPanelCity)"></i>
          {{ detailPanelCity.name }} 상세 기상 정보
        </h2>

        <div class="panel-info-grid">
          <div class="panel-info-item">
            <span class="panel-info-label">현재 날씨</span>
            <span class="panel-info-value">{{ detailPanelCity.condition }}</span>
          </div>
          <div class="panel-info-item">
            <span class="panel-info-label">실시간 기온</span>
            <span class="panel-info-value">{{ convertTemp(detailPanelCity.temp) }}{{ unitSymbol }}</span>
          </div>
          <div class="panel-info-item">
            <span class="panel-info-label">체감 온도</span>
            <span class="panel-info-value">{{ convertTemp(detailPanelCity.feel) }}{{ unitSymbol }}</span>
          </div>
          <div class="panel-info-item">
            <span class="panel-info-label">습도</span>
            <span class="panel-info-value">{{ detailPanelCity.humidity }}%</span>
          </div>
          <div class="panel-info-item">
            <span class="panel-info-label">바람 속도</span>
            <span class="panel-info-value">{{ detailPanelCity.wind }}m/s</span>
          </div>
          <div class="panel-info-item">
            <span class="panel-info-label">온도 상태</span>
            <span class="panel-info-value">
              <span v-if="detailPanelCity.temp >= 25" class="label hot">🔥 더움</span>
              <span v-else class="label cool">❄️ 선선함</span>
            </span>
          </div>
        </div>

        <!-- ✅ [과제 8 추가] 5일 예보 -->
        <h3 class="forecast-title">📅 5일 예보</h3>
        <el-skeleton v-if="isForecastLoading" :rows="5" animated class="forecast-skeleton" />
        <ul v-else class="forecast-list">
          <li v-for="day in forecast" :key="day.date" class="forecast-row">
            <span class="forecast-date">{{ formatForecastDate(day.date) }}</span>
            <i class="wi weather-icon" :class="getWeatherIconClass(day)"></i>
            <span class="forecast-condition">{{ day.condition }}</span>
            <span class="forecast-temp">{{ convertTemp(day.temp) }}{{ unitSymbol }}</span>
          </li>
        </ul>
      </aside>
    </Transition>
    </Teleport>
  </div>
</template>

<style scoped>
.bg-layer {
  position: fixed;
  inset: 0;
  width: 100%;
  height: 100%;
}

.bg-layer.next { z-index: -2; }

.bg-layer.current {
  z-index: -1;
  opacity: 1;
  transition: opacity 0.8s ease;
}

.bg-layer.current.fading { opacity: 0; }

.container {
  max-width: 640px;
  margin: 0 auto;
  font-family: 'Apple SD Gothic Neo', sans-serif;
}

.page-title {
  text-align: center;
  color: var(--text-color);
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 28px;
  transition: color 0.8s ease;
}

.glass-card {
  background: var(--card-bg);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid var(--card-border);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  transition: background 0.8s ease, border 0.8s ease;
}

.section-title {
  color: var(--text-color);
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 14px;
  transition: color 0.8s ease;
}

/* ✅ [과제 7 수정] 네이티브 input → el-input으로 바뀌면서 실제 스타일 대상이
   .el-input__wrapper/.el-input__inner로 옮겨감. 값(색상/여백/둥근 정도)은 기존과 동일하게 유지. */
.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  padding: 12px 16px;
  background: var(--input-bg);
  border: 1px solid var(--input-border);
  border-radius: 12px;
  box-shadow: none;
  transition: background 0.8s ease, border 0.8s ease;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  background: var(--input-bg);
  box-shadow: none;
}

.search-input :deep(.el-input__inner) {
  font-size: 14px;
  color: var(--text-color);
  background: transparent;
  transition: color 0.8s ease;
}

.search-input :deep(.el-input__inner::placeholder) { color: var(--input-placeholder); }
.search-input :deep(.el-input__prefix) { color: var(--input-placeholder); }

/* ✅ 한글 조합 입력 안정성을 위한 네이티브 input — el-input과 동일한 룩앤필로 직접 구성 */
.native-search-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding: 12px 16px;
  box-sizing: border-box;
  background: var(--input-bg);
  border: 1px solid var(--input-border);
  border-radius: 12px;
  transition: background 0.8s ease, border 0.8s ease;
}

.native-search-input {
  flex: 1;
  min-width: 0;
  border: none;
  outline: none;
  background: transparent;
  font-size: 14px;
  color: var(--text-color);
  transition: color 0.8s ease;
}

.native-search-input::placeholder {
  color: var(--input-placeholder);
}

.weather-alert { margin-bottom: 20px; }

.weather-skeleton {
  padding: 24px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 20px;
}

.weather-empty :deep(.el-empty__description p) {
  color: var(--text-color);
}

/* ✅ [과제 8 추가] weather-icons 아이콘 */
.weather-icon {
  margin-right: 6px;
}

/* ✅ [과제 8 추가] 한국/세계 검색 토글 */
.search-mode-toggle {
  display: flex;
  margin-bottom: 14px;
}

.search-mode-toggle :deep(.el-radio-button__inner) {
  background: var(--input-bg);
  border-color: var(--input-border);
  color: var(--text-sub);
  box-shadow: none;
}

.search-mode-toggle :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: var(--btn-bg);
  border-color: var(--btn-border);
  color: var(--btn-color);
  box-shadow: none;
  font-weight: 700;
}

/* ✅ [과제 8 추가] 세계 도시 검색 */
.world-search-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.world-search-row .search-input {
  flex: 1;
}

.world-search-btn.el-button {
  padding: 8px 20px;
  height: auto;
  background: var(--btn-bg);
  border: 1px solid var(--btn-border);
  color: var(--btn-color);
  font-weight: 600;
  white-space: nowrap;
}

.world-search-btn.el-button:hover,
.world-search-btn.el-button:focus {
  background: var(--btn-bg);
  border-color: var(--btn-border);
  color: var(--btn-color);
  opacity: 0.8;
}

.world-search-alert {
  margin-top: 14px;
  margin-bottom: 0;
}

.world-result-card {
  position: relative;
  overflow: hidden;
  margin-top: 14px;
  margin-bottom: 0;
  cursor: default;
}

.world-result-card:hover {
  transform: none;
}

/* ✅ [과제 8 수정] flag-icons 국기를 카드 배경으로 은은하게 깔기
   — cover로 늘리면 국기 비율이 깨져서 색 블록처럼 보이는 문제가 있어
   원래 비율(contain)을 유지한 채 카드 오른쪽에 배치 (왼쪽 텍스트와 안 겹침) */
.world-flag-bg {
  /* .fi 기본 클래스가 display:inline-block + width:1.333em이라 inset만으로는
     안 늘어나서 block + 100%로 명시적으로 덮어씀 */
  display: block;
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background-size: contain;
  background-position: right center;
  background-repeat: no-repeat;
  opacity: 0.45;
  z-index: 0;
}

.world-result-card .card-info {
  position: relative;
  z-index: 1;
}

.search-status {
  color: var(--text-sub);
  font-size: 13px;
  margin-top: 8px;
  transition: color 0.8s ease;
}

.world-search-hint {
  margin-top: 10px;
}

/* ✅ [과제 8 추가] 검색창 접두 아이콘을 이모지로 표시 */
.search-mode-emoji {
  font-size: 15px;
  line-height: 1;
}

.weather-section { margin-bottom: 20px; }

.no-result {
  text-align: center;
  color: var(--text-color);
  padding: 30px;
  background: var(--card-bg);
  backdrop-filter: blur(16px);
  border: 1px solid var(--card-border);
  border-radius: 20px;
}

.weather-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  margin-bottom: 12px;
  background: var(--card-bg);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid var(--card-border);
  border-radius: 20px;
  cursor: pointer;
  transition: background 0.2s, transform 0.15s, border 0.8s ease;
}

.weather-card:hover {
  transform: translateY(-2px);
}

.city-name {
  color: var(--text-color);
  font-weight: 700;
  font-size: 16px;
  margin-bottom: 4px;
  transition: color 0.8s ease;
}

.city-temp {
  color: var(--text-sub);
  font-size: 13px;
  margin-bottom: 4px;
  transition: color 0.8s ease;
}

.city-humidity {
  color: var(--text-sub);
  font-size: 13px;
  margin-bottom: 10px;
  transition: color 0.8s ease;
}

.label {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.hot { background: rgba(220, 80, 60, 0.85); color: white; }
.cool { background: rgba(60, 140, 220, 0.85); color: white; }

.detail-btn {
  padding: 8px 16px;
  background: var(--btn-bg);
  border: 1px solid var(--btn-border);
  border-radius: 12px;
  color: var(--btn-color);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.2s, color 0.8s ease, border 0.8s ease;
}

.detail-btn:hover { opacity: 0.8; }

.status-section {
  text-align: center;
  padding: 16px;
  margin-bottom: 16px;
  background: var(--card-bg);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid var(--card-border);
  border-radius: 16px;
  color: var(--text-color);
  font-size: 14px;
  font-weight: 500;
  transition: background 0.8s ease, color 0.8s ease, border 0.8s ease;
}

/* ✅ 상세보기 슬라이드 패널 */
.detail-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.35);
  z-index: 200;
}

.detail-panel {
  position: fixed;
  top: 0;
  right: 0;
  height: 100%;
  width: min(400px, 90vw);
  box-sizing: border-box;
  padding: 32px 28px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-left: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: -8px 0 30px rgba(0, 0, 0, 0.25);
  z-index: 201;
}

.panel-close {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.5);
  background: rgba(255, 255, 255, 0.25);
  color: white;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.panel-close:hover { background: rgba(255, 255, 255, 0.4); }

.panel-title {
  color: white;
  font-size: 20px;
  font-weight: 700;
  margin: 8px 40px 24px 0;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.panel-info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.panel-info-item {
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 14px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.panel-info-label {
  color: rgba(255, 255, 255, 0.7);
  font-size: 12px;
}

.panel-info-value {
  color: white;
  font-size: 18px;
  font-weight: 700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

/* ✅ [과제 8 추가] 5일 예보 */
.forecast-title {
  color: white;
  font-size: 15px;
  font-weight: 700;
  margin: 24px 0 12px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.forecast-skeleton {
  padding: 16px;
  background: rgba(255, 255, 255, 0.18);
  border-radius: 14px;
}

.forecast-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.forecast-row {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 12px;
  padding: 10px 14px;
}

.forecast-date {
  color: white;
  font-size: 13px;
  font-weight: 600;
  width: 62px;
  flex-shrink: 0;
}

.forecast-condition {
  color: rgba(255, 255, 255, 0.85);
  font-size: 13px;
  flex: 1;
}

.forecast-temp {
  color: white;
  font-size: 14px;
  font-weight: 700;
}

.panel-slide-enter-active,
.panel-slide-leave-active {
  transition: transform 0.35s ease;
}

.panel-slide-enter-from,
.panel-slide-leave-to {
  transform: translateX(100%);
}

.panel-overlay-fade-enter-active,
.panel-overlay-fade-leave-active {
  transition: opacity 0.35s ease;
}

.panel-overlay-fade-enter-from,
.panel-overlay-fade-leave-to {
  opacity: 0;
}
</style>