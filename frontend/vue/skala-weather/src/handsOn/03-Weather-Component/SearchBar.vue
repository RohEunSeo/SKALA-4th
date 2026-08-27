<script setup>
// props 반환값을 변수에 담지 않고 그냥 선언만 해도 template에서 바로 쓸 수 있음
defineProps({
  searchQuery: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update-query'])

const handleInput = (e) => {
  emit('update-query', e.target.value)
}
</script>

<template>
  <div class="search-bar">
    <h2 class="search-title">🔍 도시 검색 (한글 즉시 동기화)</h2>
    <!-- :value로 단방향 바인딩 + @input으로 emit — v-model 원리 직접 구현 -->
    <input
      type="text"
      :value="searchQuery"
      @input="handleInput"
      placeholder="검색할 도시 이름 입력"
      class="search-input"
    />
    <p v-if="searchQuery" class="search-status">검색 중인 도시: {{ searchQuery }}</p>
  </div>
</template>

<style scoped>
.search-title {
  color: white;
  font-size: 16px;
  margin-bottom: 12px;
  font-weight: 600;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  font-size: 14px;
  background: rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  color: white;
  outline: none;
  box-sizing: border-box;
  transition: background 0.2s;
}

.search-input::placeholder { color: rgba(255, 255, 255, 0.7); }
.search-input:focus { background: rgba(255, 255, 255, 0.4); }

.search-status {
  color: white;
  font-size: 13px;
  margin-top: 8px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}
</style>