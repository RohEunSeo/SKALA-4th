<script setup>
import { ref } from 'vue'

let normalCount = 0
const vueCount = ref(0)
</script>

<template>
  <div>
    <h3>일반 변수: {{ normalCount }}</h3>
    <button @click="normalCount++">일반 변수 증가</button>

    <h3>반응형 변수: {{ vueCount }}</h3>
    <button @click="vueCount++">Vue 변수 증가</button>
  </div>
</template>