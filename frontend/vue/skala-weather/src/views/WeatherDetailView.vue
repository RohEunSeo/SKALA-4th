<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
// ✅ [과제 5 추가] configStore import
import { useConfigStore } from '@/stores/configStore'
// ✅ [과제 6 추가] weatherStore import
import { useWeatherStore } from '@/stores/weatherStore'
import { storeToRefs } from 'pinia'

const route = useRoute()
const router = useRouter()

// ✅ [과제 5 추가] configStore 인스턴스 + 반응형 추출
const configStore = useConfigStore()
const { unit, unitSymbol } = storeToRefs(configStore)

// ✅ [과제 6 추가] weatherStore 인스턴스 + 반응형 추출 (mockData 대체)
const weatherStore = useWeatherStore()
const { weatherList, isLoading } = storeToRefs(weatherStore)

// ✅ [과제 6 수정] API 조회는 비동기라 computed로 반응형 조회 (weatherList 갱신 시 자동 반영)
const cityData = computed(() => {
  const cityId = route.params.cityId
  return weatherList.value.find((c) => c.id === cityId) ?? null
})

// ✅ [과제 5 추가] 온도 변환 computed — 슬라이드 코드 그대로
const displayTemp = computed(() => {
  if (!cityData.value) return 0
  const rawTemp = cityData.value.temp
  if (unit.value === 'fahrenheit') {
    return Math.round((rawTemp * 9) / 5 + 32)
  }
  return rawTemp
})

// ✅ [과제 5 추가] 체감 온도도 동일하게 변환
const displayFeel = computed(() => {
  if (!cityData.value) return 0
  const rawFeel = cityData.value.feel
  if (unit.value === 'fahrenheit') {
    return Math.round((rawFeel * 9) / 5 + 32)
  }
  return rawFeel
})

const goHome = () => {
  router.push('/')
}
</script>

<template>
  <div class="container">
    <!-- ✅ [과제 6 추가] API 로딩 중 표시 -->
    <div v-if="isLoading" class="not-found">
      <p>⏳ 날씨 정보를 불러오는 중...</p>
    </div>

    <div v-else-if="!cityData" class="not-found">
      <p>🌫️ 해당 도시 정보를 찾을 수 없습니다.</p>
      <button @click="goHome" class="back-btn">메인으로 돌아가기</button>
    </div>

    <div v-else class="detail-card">
      <h2 class="city-title">📍 {{ cityData.name }} 상세 기상 정보</h2>

      <!-- ✅ [과제 7 수정] info-grid → el-descriptions로 교체 -->
      <el-descriptions :column="2" border class="weather-descriptions">
        <el-descriptions-item label="현재 날씨">{{ cityData.status }}</el-descriptions-item>
        <!-- ✅ [과제 5 수정] displayTemp + unitSymbol로 단위 변환 표시 -->
        <el-descriptions-item label="실시간 기온">{{ displayTemp }}{{ unitSymbol }}</el-descriptions-item>
        <!-- ✅ [과제 5 수정] displayFeel + unitSymbol로 단위 변환 표시 -->
        <el-descriptions-item label="체감 온도">{{ displayFeel }}{{ unitSymbol }}</el-descriptions-item>
        <el-descriptions-item label="습도">{{ cityData.humidity }}%</el-descriptions-item>
        <el-descriptions-item label="바람 속도">{{ cityData.wind }}m/s</el-descriptions-item>
        <el-descriptions-item label="온도 상태">
          <span v-if="cityData.temp >= 25" class="label hot">🔥 더움</span>
          <span v-else class="label cool">❄️ 선선함</span>
        </el-descriptions-item>
      </el-descriptions>

      <!-- ✅ [과제 7 수정] back-btn → el-button(type=primary round)으로 교체 -->
      <el-button type="primary" round @click="goHome" class="back-btn">← 메인 대시보드로 이동</el-button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 640px;
  margin: 0 auto;
  font-family: 'Apple SD Gothic Neo', sans-serif;
}

.detail-card {
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 24px;
  padding: 32px;
}

.city-title {
  color: white;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 24px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 28px;
}

.info-item {
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 14px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-label {
  color: rgba(255, 255, 255, 0.7);
  font-size: 12px;
}

.info-value {
  color: white;
  font-size: 18px;
  font-weight: 700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
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

.back-btn {
  width: 100%;
  padding: 14px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.back-btn:hover { background: rgba(255, 255, 255, 0.4); }

/* ✅ [과제 7 추가] info-grid → el-descriptions 대체 — 기존 글래스 카드 톤 유지 */
.weather-descriptions {
  margin-bottom: 28px;
}

.weather-descriptions :deep(.el-descriptions__body) {
  background: transparent;
}

.weather-descriptions :deep(.el-descriptions__table) {
  border-color: rgba(255, 255, 255, 0.35);
}

.weather-descriptions :deep(.el-descriptions__cell) {
  background: rgba(255, 255, 255, 0.18);
  border-color: rgba(255, 255, 255, 0.35);
  padding: 12px 16px;
}

.weather-descriptions :deep(.el-descriptions__label) {
  color: rgba(255, 255, 255, 0.7);
  font-size: 12px;
  font-weight: 400;
}

.weather-descriptions :deep(.el-descriptions__content) {
  color: white;
  font-size: 16px;
  font-weight: 700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

/* ✅ [과제 7 추가] back-btn이 el-button(type=primary round)으로 바뀌면서
   Element 기본 파란색 대신 기존 반투명 글래스 버튼 톤을 유지하도록 오버라이드 */
.back-btn.el-button {
  width: 100%;
  height: auto;
  padding: 14px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  color: white;
  font-size: 14px;
  font-weight: 600;
}

.back-btn.el-button:hover,
.back-btn.el-button:focus {
  background: rgba(255, 255, 255, 0.4);
  border-color: rgba(255, 255, 255, 0.5);
  color: white;
}

.not-found {
  text-align: center;
  color: white;
  padding: 60px 20px;
}

.not-found p {
  font-size: 18px;
  margin-bottom: 20px;
}
</style>