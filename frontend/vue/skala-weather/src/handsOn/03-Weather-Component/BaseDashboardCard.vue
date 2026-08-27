<script setup>
// 이 컴포넌트는 데이터가 없음 — 글래스 카드 껍데기 역할만 함
// 안에 뭘 넣을지는 부모가 slot으로 꽂아 넣어줌
</script>

<template>
  <!-- 글래스모피즘 카드 껍데기 -->
  <!-- <slot />이 있는 자리에 부모가 넣어준 HTML이 렌더링됨 -->
  <div class="dashboard-card">
    <slot />
  </div>
</template>

<style scoped>
.dashboard-card {
  max-width: 600px;
  margin: 0 auto 20px;
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  padding: 24px;
}
</style>