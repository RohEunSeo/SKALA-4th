<script setup>
import { ref } from 'vue'

// ── 화면에 결과를 표시할 반응형 변수들 ────────────────────────
const position = ref('')   // 클릭 좌표 출력용
const tagName = ref('')    // 클릭된 태그 + 파라미터 출력용

// ── 패턴 ①: 이벤트 객체만 받을 때 ───────────────────────────
// 함수 이름만 @click에 넘기면 JS 엔진이 첫 번째 인자로 Event Object를 자동으로 넘겨줌
// 파라미터 이름은 e, event 뭐든 상관없음. 관례적으로 e 또는 event를 씀
const getOnlyEvent = (e) => {
  // e.clientX, e.clientY : 브라우저 화면(Viewport) 기준 클릭 좌표
  position.value = `좌표: X=${e.clientX}, Y=${e.clientY}`
}

// ── 패턴 ②: 내 커스텀 데이터 + 이벤트 객체 둘 다 받을 때 ────
// 인라인에서 함수를 직접 호출할 때는 $event를 명시적으로 써야 이벤트 객체가 전달됨
// $event : Vue가 제공하는 특수 기호. 현재 이벤트 객체를 가리킴
const getWithParam = (name, e) => {
  // e.target : 실제로 클릭된 HTML 요소
  // e.target.tagName : 그 요소의 태그 이름 (예: BUTTON, DIV, INPUT)
  tagName.value = `대상: ${name} / 클릭된 태그: ${e.target.tagName}`
}
</script>

<template>
  <div style="padding: 20px">
    <h2>v-on 이벤트 객체($event) 활용</h2>

    <p>좌표: {{ position }}</p>
    <p>태그: {{ tagName }}</p>

    <!-- 패턴 ①: 함수 이름만 넘기면 Event Object 자동 전달 -->
    <button @click="getOnlyEvent">클릭 좌표 알아내기</button>

    &nbsp;

    <!-- 패턴 ②: 커스텀 데이터('회원A')와 이벤트 객체($event)를 동시에 넘김 -->
    <!-- $event를 명시적으로 써야 이벤트 객체가 함께 전달됨 -->
    <button @click="getWithParam('회원A', $event)">회원 정보와 태그 확인</button>
  </div>
</template>