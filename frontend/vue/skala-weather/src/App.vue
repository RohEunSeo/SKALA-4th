<script setup>
import { RouterLink, RouterView } from 'vue-router'
import UnitToggler from '@/components/UnitToggler.vue'
// ✅ [과제 7 추가] API 에러 전역 배너 표시용
import { useWeatherStore } from '@/stores/weatherStore'
import { storeToRefs } from 'pinia'

const weatherStore = useWeatherStore()
const { error } = storeToRefs(weatherStore)
</script>

<template>
  <div class="app-wrapper">
    <!-- 배경 레이어 — 전역으로 항상 깔려있음 -->
    <div class="app-bg"></div>

    <!-- 네비게이션 바 — 페이지 이동해도 항상 상단에 고정 -->
    <nav class="navbar">
      <!-- RouterLink: <a> 태그 대신 씀. 페이지 새로고침 없이 이동 -->
      <!-- router-link-active: 현재 경로와 일치하면 자동으로 붙는 클래스 -->
      <RouterLink to="/" class="nav-logo">🌤️ 날씨 앱</RouterLink>
      <div class="nav-links">
        <RouterLink to="/">날씨 대시보드</RouterLink>
        <RouterLink to="/about">서비스 소개</RouterLink>
        <RouterLink to="/stats">날씨 통계</RouterLink>
      </div>
      <UnitToggler />
    </nav>

    <!-- ✅ [과제 7 추가] API 에러 발생 시 페이지 상관없이 항상 보이는 전역 배너 -->
    <el-alert
      v-if="error"
      title="날씨 데이터를 불러오는 중 문제가 발생했습니다."
      type="error"
      show-icon
      class="global-error-alert"
    />

    <!-- RouterView: 현재 URL에 맞는 컴포넌트가 여기 렌더링됨 -->
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-wrapper {
  position: relative;
  min-height: 100vh;
}

/* 전체 배경 그라디언트 */
.app-bg {
  position: fixed;
  inset: 0;
  background: linear-gradient(160deg, #f7971e, #ffd200, #56ccf2);
  z-index: 0;
}

/* 네비게이션 바 */
.navbar {
  position: relative;
  z-index: 10;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 40px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
}

.nav-logo {
  color: white;
  font-size: 18px;
  font-weight: 700;
  text-decoration: none;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.nav-links {
  display: flex;
  gap: 24px;
}

.nav-links a {
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  padding: 6px 14px;
  border-radius: 20px;
  transition: background 0.2s, color 0.2s;
}

.nav-links a:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

/* 현재 페이지 링크에 자동으로 붙는 클래스 */
.nav-links a.router-link-active {
  background: rgba(255, 255, 255, 0.3);
  color: white;
  font-weight: 700;
}

.main-content {
  position: relative;
  z-index: 1;
  padding: 40px 20px;
}

/* ✅ [과제 7 추가] 전역 에러 배너 위치 */
.global-error-alert {
  position: relative;
  z-index: 5;
  margin: 16px 40px 0;
}
</style>