<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
</script>

<template>
  <div class="container">
    <div class="notfound-card">
      <p class="emoji">🌫️ ❓</p>
      <h2 class="title">페이지를 찾을 수 없습니다.</h2>
      <p class="desc">
        요청하신 주소가 존재하지 않거나,<br />
        이동 페이지가 변경되었을 수 있습니다.
      </p>
      <button @click="router.push('/')" class="home-btn">날씨 메인으로 이동</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 640px;
  margin: 0 auto;
  font-family: 'Apple SD Gothic Neo', sans-serif;
}

.notfound-card {
  text-align: center;
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 24px;
  padding: 60px 32px;
}

.emoji {
  font-size: 52px;
  margin-bottom: 16px;
}

.title {
  color: white;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 12px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.desc {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  line-height: 1.8;
  margin-bottom: 28px;
}

.home-btn {
  padding: 12px 28px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 14px;
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.home-btn:hover { background: rgba(255, 255, 255, 0.4); }
</style>