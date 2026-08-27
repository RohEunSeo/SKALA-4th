<script setup>
import { ref } from 'vue'

// ref()로 다양한 타입의 반응형 변수 선언
// script 안에서는 .value로 접근해야 함
const count = ref(0)
const name = ref('홍길동')
const isActive = ref(true)
const items = ref(['사과', '배'])
const user = ref({ name: '이순신', age: 30 })

// 카운트 증가 함수
const increaseRef = () => {
  count.value++
}

// 사용자 이름 변경 함수
// 객체 내부 속성 변경할 때도 .value 먼저 붙여야 함
const changeUserName = () => {
  user.value.name = '장보고'
}
</script>

<template>
  <div style="padding: 20px">
    <h2>반응형 상태 ref() 기초</h2>

    <!-- template 안에서는 .value 없이 바로 씀 -->
    <p>Ref 카운트: <strong>{{ count }}</strong></p>

    <!-- v-model로 name 변수와 양방향 바인딩 -->
    <p>
      이름: <input v-model="name" />
      {{ name }}
    </p>

    <!-- 삼항연산자로 isActive 상태에 따라 텍스트 변경 -->
    <p>활성 상태: {{ isActive ? '활성' : '비활성' }}</p>

    <!-- join()으로 배열을 문자열로 변환해서 출력 -->
    <p>과일 목록: {{ items.join(', ') }}</p>

    <!-- 객체 내부 속성도 .value 없이 바로 접근 -->
    <p>사용자 정보: 이름 - {{ user.name }}, 나이 - {{ user.age }}</p>

    <button @click="increaseRef">Ref 변수 증가</button>
    <!-- 인라인으로 바로 처리 -->
    <button @click="isActive = !isActive">토글</button>
    <button @click="items.push('귤')">과일 추가</button>
    <button @click="changeUserName">사용자 이름 변경</button>
  </div>
</template>