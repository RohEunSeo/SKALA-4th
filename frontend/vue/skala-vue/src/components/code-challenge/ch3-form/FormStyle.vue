<script setup>
// 자바스크립트 방은 비워둠 — 이 컴포넌트는 스타일 실습용이라 데이터 없음
</script>

<template>
  <div class="practice-section">
    <h2>Scoped 스타일 및 외부 CSS 활용</h2>

    <!-- .title 클래스 → 아래 <style scoped>에서 정의한 스타일만 적용 -->
    <!-- scoped라서 다른 컴포넌트의 .title에는 절대 영향 안 줌 -->
    <p class="title">이 글자는 이 컴포넌트 내부에서만 빨간색이 됩니다.</p>

    <!-- .btn-external 클래스 → @import로 불러온 외부 CSS에서 스타일 적용 -->
    <button class="btn-external">외부 CSS에서 불러온 버튼 스타일</button>
  </div>
</template>

<!-- scoped: 이 컴포넌트 안에서만 적용되는 전용 스타일 -->
<!-- Vue가 빌드할 때 .title[data-v-xxxxxx] 처럼 고유 해시를 자동으로 붙여줌 -->
<!-- 그래서 다른 컴포넌트의 .title 클래스와 절대 충돌하지 않음 -->
<style scoped>
.title {
  color: #ff7675;
  font-weight: bold;
  font-size: 18px;
}
</style>

<!-- scoped 없이: 외부 CSS 파일을 이 컴포넌트에서 불러옴 -->
<!-- src/assets/ 안에 challenge.css 파일 있어야 동작함 -->
<style>
@import '@/assets/challenge.css';
</style>