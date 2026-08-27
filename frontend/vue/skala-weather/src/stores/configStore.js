import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useConfigStore = defineStore('config', () => {
  // state — 현재 날씨 단위 저장 (초기값: 섭씨)
  const unit = ref('celsius')

  // getters — 현재 단위에 맞는 기호 반환
  const unitSymbol = computed(() => {
    return unit.value === 'celsius' ? '°C' : '°F'
  })

  // actions — 섭씨 ↔ 화씨 토글
  const toggleUnit = () => {
    unit.value = unit.value === 'celsius' ? 'fahrenheit' : 'celsius'
  }

  return { unit, unitSymbol, toggleUnit }
})