<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
// ✅ [과제 6 추가] weatherStore import
import { useWeatherStore } from '@/stores/weatherStore'
import { storeToRefs } from 'pinia'

const router = useRouter()

// ✅ [과제 6 추가] weatherStore 인스턴스 + 반응형 추출 (하드코딩 목록 대체)
const weatherStore = useWeatherStore()
const { weatherList } = storeToRefs(weatherStore)

// 평균 기온
const avgTemp = computed(() => {
  if (weatherList.value.length === 0) return '0.0'
  const total = weatherList.value.reduce((sum, c) => sum + c.temp, 0)
  return (total / weatherList.value.length).toFixed(1)
})

// 가장 더운 도시
const hottestCity = computed(() => {
  if (weatherList.value.length === 0) return { name: '-', temp: 0 }
  return weatherList.value.reduce((prev, curr) => (prev.temp > curr.temp ? prev : curr))
})

// 가장 선선한 도시
const coolestCity = computed(() => {
  if (weatherList.value.length === 0) return { name: '-', temp: 0 }
  return weatherList.value.reduce((prev, curr) => (prev.temp < curr.temp ? prev : curr))
})

// 더운 도시 수 (25도 이상)
const hotCount = computed(() => weatherList.value.filter((c) => c.temp >= 25).length)

// 선선한 도시 수 (25도 미만)
const coolCount = computed(() => weatherList.value.filter((c) => c.temp < 25).length)
</script>

<template>
  <div class="container">
    <div class="stats-card">
      <h2 class="title">📊 날씨 통계</h2>

      <div class="stats-grid">
        <!-- ✅ [과제 7 수정] 숫자 값 → el-statistic으로 교체 -->
        <div class="stat-item">
          <span class="stat-label">전체 도시 평균 기온</span>
          <el-statistic :value="Number(avgTemp)" :precision="1" suffix="°C" class="stat-value" />
        </div>
        <!-- 가장 더운/선선한 도시는 기존 텍스트 유지 -->
        <div class="stat-item">
          <span class="stat-label">가장 더운 도시</span>
          <span class="stat-value">{{ hottestCity.name }} ({{ hottestCity.temp }}°C)</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">가장 선선한 도시</span>
          <span class="stat-value">{{ coolestCity.name }} ({{ coolestCity.temp }}°C)</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">더운 도시 수 (25°C↑)</span>
          <el-statistic :value="hotCount" suffix="개" class="stat-value hot">
            <template #prefix>🔥</template>
          </el-statistic>
        </div>
        <div class="stat-item">
          <span class="stat-label">선선한 도시 수 (25°C↓)</span>
          <el-statistic :value="coolCount" suffix="개" class="stat-value cool">
            <template #prefix>❄️</template>
          </el-statistic>
        </div>
        <div class="stat-item">
          <span class="stat-label">전체 도시 수</span>
          <el-statistic :value="weatherList.length" suffix="개" class="stat-value" />
        </div>
      </div>

      <button @click="router.push('/')" class="home-btn">← 메인 대시보드로 이동</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 640px;
  margin: 0 auto;
  font-family: 'Apple SD Gothic Neo', sans-serif;
}

.stats-card {
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 24px;
  padding: 32px;
}

.title {
  color: rgba(50, 50, 50, 0.9);
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 24px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
  margin-bottom: 28px;
}

.stat-item {
  background: rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 14px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-label {
  color: rgba(50, 50, 50, 0.6);
  font-size: 12px;
}

.stat-value {
  color: rgba(50, 50, 50, 0.7);
  font-size: 16px;
  font-weight: 700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.stat-value.hot { color: #ff9a8a; }
.stat-value.cool { color: #70baff; }

/* ✅ [과제 7 추가] el-statistic 오버라이드 — 숫자는 white 유지, hot/cool은 기존 강조색 유지 */
.stat-value :deep(.el-statistic__content) {
  display: flex;
  align-items: baseline;
  gap: 4px;
  font-size: 16px;
  font-weight: 700;
}

.stat-value :deep(.el-statistic__number),
.stat-value :deep(.el-statistic__suffix) {
  color: white;
  font-size: 16px;
  font-weight: 700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.stat-value.hot :deep(.el-statistic__number),
.stat-value.hot :deep(.el-statistic__suffix) { color: #ff9a8a; }

.stat-value.cool :deep(.el-statistic__number),
.stat-value.cool :deep(.el-statistic__suffix) { color: #70baff; }

.home-btn {
  width: 100%;
  padding: 14px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  color: rgba(50, 50, 50, 0.8);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.home-btn:hover { background: rgba(255, 255, 255, 0.4); }
</style>