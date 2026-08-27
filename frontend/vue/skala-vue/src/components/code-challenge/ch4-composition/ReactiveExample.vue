<script setup>
import { reactive } from 'vue'

// reactive()로 객체와 배열을 반응형으로 선언
// ref()와 달리 .value 없이 바로 속성에 접근 가능
const userReactive = reactive({ name: '이순신', age: 30 })

// 나이 증가 함수
// reactive는 .value 없이 바로 속성을 변경함
const celebrateReactive = () => {
  userReactive.age++
}

// 배열도 reactive로 선언 가능
// ⚠️ 단, items = ['a', 'b'] 처럼 통째로 재할당하면 반응성이 끊어짐
// push/splice로 내부를 수정해야 함
const items = reactive(['사과', '바나나'])

const addItem = () => {
  items.push(`과일 ${items.length + 1}`)
}

const removeItem = (index) => {
  items.splice(index, 1)
}
</script>

<template>
  <div style="padding: 20px">
    <h2>반응형 상태 reactive() 특징 및 주의점</h2>

    <!-- reactive는 template에서도 .value 없이 바로 씀 -->
    <h3>1) 객체(Object) reactive</h3>
    <p>이름: {{ userReactive.name }} / 나이: {{ userReactive.age }}세</p>
    <button @click="celebrateReactive">reactive 나이 한 살 추가</button>

    <br /><br />

    <h3>2) 배열(Array) reactive</h3>
    <ul>
      <li v-for="(item, index) in items" :key="index">
        {{ item }}
        <button @click="removeItem(index)">삭제</button>
      </li>
    </ul>
    <button @click="addItem">과일 항목 추가</button>
  </div>
</template>