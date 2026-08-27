<script setup>
import { ref } from 'vue'

// ── 각 form 요소에 맞는 타입으로 ref 초기값 선언 ────────────────
const text1 = ref('')       // v-model 양방향 바인딩용
const text2 = ref('')       // v-model 내부 동작 원리 이해용 (단방향 + 이벤트)

const comment = ref('')     // textarea — 문자열로 시작
const isAgreed = ref(false) // 단일 checkbox — 반드시 boolean으로 시작
const favoriteFruits = ref([]) // 다중 checkbox — 반드시 배열로 시작! 안 그러면 버그남
const gender = ref('')      // radio — 문자열로 시작
const selectedCar = ref('') // select — 문자열로 시작
</script>

<template>
  <div style="padding: 20px">
    <h2>v-model 양방향 데이터 바인딩</h2>

    <!-- ── v-model 기본 동작 ──────────────────────────────────── -->
    <h3>1) v-model 축약 문법 (양방향)</h3>
    <!-- v-model 한 줄 = :value + @input 을 합친 것 -->
    <!-- 입력창에 타이핑하면 text1 변수가 즉시 바뀌고, 화면도 즉시 바뀜 -->
    <input type="text" v-model="text1" placeholder="여기에 입력하세요" />
    <p>입력된 값: <strong>{{ text1 }}</strong></p>

    <h3>2) v-model의 내부 작동 원리 (단방향 + 이벤트)</h3>
    <!-- v-model이 내부적으로 이렇게 동작함 -->
    <!-- :value → 데이터를 화면에 보여줌 (단방향) -->
    <!-- @input → 타이핑할 때마다 e.target.value를 변수에 반영 -->
    <input
      type="text"
      :value="text2"
      @input="(e) => (text2 = e.target.value)"
      placeholder="원리 파악용 입력창"
    />
    <p>입력된 값: <strong>{{ text2 }}</strong></p>

    <hr />

    <!-- ── HTML Form 요소별 v-model 매핑 ─────────────────────── -->
    <h2>모든 HTML Form 요소와 v-model 매핑</h2>

    <!-- 1) Textarea — 장문 텍스트, ref('') 문자열로 선언 -->
    <h3>1) Textarea (장문 텍스트)</h3>
    <textarea v-model="comment" placeholder="의견을 남겨주세요"></textarea>
    <p>데이터 상태: <span>{{ comment }}</span></p>

    <!-- 2) 단일 Checkbox — 체크 여부만 저장, ref(false) boolean으로 선언 -->
    <h3>2) 단일 Checkbox (동의 여부)</h3>
    <label>
      <input type="checkbox" v-model="isAgreed" /> 약관에 동의합니다.
    </label>
    <p>데이터 상태: <span>{{ isAgreed }}</span></p>

    <!-- 3) 다중 Checkbox — 선택된 항목의 value가 배열에 자동으로 추가/제거됨 -->
    <!-- ref([]) 배열로 선언 안 하면 체크박스가 이상하게 동작함 -->
    <h3>3) 다중 Checkbox (복수 선택 → 배열에 저장)</h3>
    <label><input type="checkbox" value="사과" v-model="favoriteFruits" /> 사과</label>
    <label><input type="checkbox" value="바나나" v-model="favoriteFruits" /> 바나나</label>
    <label><input type="checkbox" value="딸기" v-model="favoriteFruits" /> 딸기</label>
    <p>데이터 상태 (배열): <span>{{ favoriteFruits }}</span></p>

    <!-- 4) Radio — 같은 v-model 변수를 공유하면 하나만 선택됨 -->
    <!-- 선택된 라디오의 value 값이 gender 변수에 들어감 -->
    <h3>4) Radio (단일 선택)</h3>
    <label><input type="radio" value="남성" v-model="gender" /> 남성</label>
    <label><input type="radio" value="여성" v-model="gender" /> 여성</label>
    <p>데이터 상태: <span>{{ gender }}</span></p>

    <!-- 5) Select — 사용자가 선택한 <option>의 value가 selectedCar에 들어감 -->
    <h3>5) Select (드롭다운 선택)</h3>
    <select v-model="selectedCar">
      <option value="">-- 선택하세요 --</option>
      <option value="tesla">테슬라</option>
      <option value="hyundai">현대자동차</option>
      <option value="bmw">BMW</option>
    </select>
    <p>데이터 상태: <span>{{ selectedCar }}</span></p>
  </div>
</template>