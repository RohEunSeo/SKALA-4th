<script setup>
import { storeToRefs } from 'pinia'
import { useConfigStore } from '@/stores/configStore'

const configStore = useConfigStore()
// state, getters는 storeToRefs()로 꺼내야 반응성 유지됨
// ✅ [과제 7 추가] el-switch의 켜짐/꺼짐 상태를 판단하기 위해 unit도 함께 추출
const { unit, unitSymbol, isDark } = storeToRefs(configStore)
// actions는 일반 구조 분해 할당 가능
const { toggleUnit, toggleTheme } = configStore
</script>

<template>
  <div class="unit-toggler">
    <!-- 날씨 단위 표시 + 전환 버튼 -->
    <span class="unit-label">날씨단위: {{ unitSymbol }}</span>
    <!-- ✅ [과제 7 수정] 단위변경 버튼 → el-switch로 교체, 기존 toggleUnit 액션 그대로 연결 -->
    <el-switch
      :model-value="unit === 'fahrenheit'"
      active-text="°F"
      inactive-text="°C"
      class="unit-switch"
      @change="toggleUnit"
    />

    <!-- ④ 본인 추가 — 테마 전환 버튼 -->
    <button @click="toggleTheme" class="theme-btn">
      {{ isDark ? '☀️' : '🌙' }}
    </button>
  </div>
</template>

<style scoped>
.unit-toggler {
  display: flex;
  align-items: center;
  gap: 10px;
}

.unit-label {
  color: rgba(255, 255, 255, 0.85);
  font-size: 13px;
}

.toggle-btn {
  padding: 6px 14px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  color: white;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.toggle-btn:hover {
  background: rgba(255, 255, 255, 0.4);
}

/* ✅ [과제 7 추가] el-switch가 네비게이션 바의 유리질감 톤에 맞도록 오버라이드 */
.unit-switch {
  --el-switch-on-color: rgba(255, 255, 255, 0.45);
  --el-switch-off-color: rgba(255, 255, 255, 0.25);
}

.unit-switch :deep(.el-switch__label) {
  color: rgba(255, 255, 255, 0.7);
}

.unit-switch :deep(.el-switch__label.is-active) {
  color: white;
}

.theme-btn {
  padding: 6px 10px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.2s;
}

.theme-btn:hover {
  background: rgba(255, 255, 255, 0.35);
}
</style>