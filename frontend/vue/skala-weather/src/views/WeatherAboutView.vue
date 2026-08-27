<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<template>
  <div class="container">
    <div class="about-card">
      <h2 class="title">🌈 서비스 소개</h2>
      <p class="description">
        본 앱은 Vue 3 와 Vue Router 4 기반 날씨 대시보드 시스템입니다.<br /><br />
        클라이언트 사이드 라우팅을 통해 새로고침 없는 부드러운 화면 전환을 제공하며,
        URL 파라미터를 활용해 도시별 상세 날씨 정보를 제공합니다.
      </p>

      <ul class="feature-list">
        <li>🌤️ 지역별 실시간 날씨 현황 대시보드</li>
        <li>🔍 도시 이름 기반 검색 필터링</li>
        <li>📍 도시별 상세 기상 정보 페이지</li>
        <li>📊 전체 도시 날씨 통계 페이지</li>
      </ul>

      <button @click="goHome" class="home-btn">대시보드로 이동</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 640px;
  margin: 0 auto;
  font-family: 'Apple SD Gothic Neo', sans-serif;
}

.about-card {
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 24px;
  padding: 32px;
}

.title {
  color: rgb(50, 50, 50, 0.9);
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 20px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.description {
  color: rgba(50, 50, 50, 0.9);
  font-size: 14px;
  line-height: 1.8;
  margin-bottom: 24px;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin-bottom: 28px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.feature-list li {
  color: rgba(50, 50, 50, 0.8);
  font-size: 14px;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
}

.home-btn {
  width: 100%;
  padding: 14px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  color: rgba(50, 50, 50, 0.9);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.home-btn:hover { background: rgba(255, 255, 255, 0.4); }
</style>