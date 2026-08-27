<script setup>
import { ref } from 'vue'

// ── 각 수식어 실습용 변수 ────────────────────────────────────
const lazyText = ref('')    // .lazy — 포커스 아웃/Enter 시에만 반영
const age = ref('')         // .number — 자동으로 Number 타입으로 변환
const userEmail = ref('')   // .trim — 앞뒤 공백 자동 제거
const price = ref('')       // .trim.number 체이닝
</script>

<template>
  <div style="padding: 20px">
    <h2>v-model 수식어 (Modifiers) 활용</h2>

    <!-- ① .lazy — 타이핑할 때마다 반영하는 게 아니라 -->
    <!-- 포커스를 잃거나(다른 곳 클릭) Enter 칠 때만 반영 -->
    <!-- 실시간 API 요청 같은 비용이 큰 작업에 유용 -->
    <h3>1) .lazy 수식어 (change 이벤트 시점 반영)</h3>
    <input type="text" v-model.lazy="lazyText" placeholder="입력 후 Enter 또는 외부 클릭" />
    <p>실시간이 아닌 확정된 값: <strong>{{ lazyText }}</strong></p>

    <!-- ② .number — input에서 받은 값은 기본적으로 String -->
    <!-- .number 붙이면 자동으로 Number 타입으로 변환해줌 -->
    <!-- typeof로 타입 확인해보면 차이 확인 가능 -->
    <h3>2) .number 수식어 (Number 타입 자동 형변환)</h3>
    <input type="text" v-model.number="age" placeholder="나이를 입력하세요" />
    <p>입력된 값: <strong>{{ age }}</strong></p>
    <p>데이터 타입: <strong>{{ typeof age }}</strong></p>

    <!-- ③ .trim — 앞뒤 공백을 자동으로 제거해줌 -->
    <!-- 이메일, 이름 같은 입력에서 실수로 공백 넣는 것 방지 -->
    <h3>3) .trim 수식어 (양끝 공백 자동 제거)</h3>
    <input type="text" v-model.trim="userEmail" placeholder="앞뒤 공백을 포함해 입력해 보세요" />
    <p>공백 제거된 값: <strong>"{{ userEmail }}"</strong></p>
    <p>문자열 길이: <strong>{{ userEmail.length }}</strong></p>

    <!-- ④ 수식어 체이닝 — 여러 수식어를 동시에 붙일 수 있음 -->
    <!-- .trim으로 공백 제거 후 .number로 숫자 변환 -->
    <h3>4) Chaining (수식어 체이닝: .trim.number)</h3>
    <input type="text" v-model.trim.number="price" placeholder="공백과 숫자를 섞어 입력해 보세요" />
    <p>처리된 값: <strong>"{{ price }}"</strong></p>
    <p>데이터 타입: <strong>{{ typeof price }}</strong></p>
  </div>
</template>