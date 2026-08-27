<script setup>
import { ref } from 'vue'

// ── Inline Handler용 반응형 변수 ──────────────────────────────
const count = ref(0)
// ref(0) : 숫자 0을 반응형으로 포장. 값이 바뀌면 화면도 자동으로 바뀜

// ── Method Handler용 함수 ────────────────────────────────────
const showAlert = () => {
  // 복잡한 로직은 여기처럼 script 안에 함수로 빼는 것이 Method Handler 방식
  alert('함수가 성공적으로 호출되었습니다!')
}
</script>

<template>
  <div style="padding: 20px">
    <h2>v-on 이벤트 핸들링 기초</h2>

    <!-- ① Inline Handler: 간단한 연산을 태그 안에서 바로 처리 -->
    <!-- @click 은 v-on:click 의 축약형 -->
    <!-- count++ 처럼 한 줄짜리 간단한 연산만 인라인으로 씀 -->
    <h3>1) 인라인 연산 처리</h3>
    <p>현재 카운트: {{ count }}</p>
    <button @click="count++">1씩 증가</button>

    <br /><br />

    <!-- ② Method Handler: 함수 이름만 넘기면 Vue가 이벤트 리스너로 자동 등록 -->
    <!-- ⚠️ showAlert() 괄호 붙이면 안 됨 → 렌더링 시 바로 실행돼버림 -->
    <!-- showAlert 이름만 넘겨야 클릭 시에 실행됨 -->
    <h3>2) 스크립트 함수 호출</h3>
    <button @click="showAlert">알림창 띠우기</button>
  </div>
</template>